package com.rikka.mall.service.impl;

import com.google.gson.Gson;
import com.rikka.mall.dao.MallProductMapper;
import com.rikka.mall.enums.ProductStatusEnum;
import com.rikka.mall.enums.ResponseStatusEnum;
import com.rikka.mall.form.CartAddForm;
import com.rikka.mall.form.CartUpdateForm;
import com.rikka.mall.pojo.CartProduct;
import com.rikka.mall.pojo.MallProduct;
import com.rikka.mall.pojo.MallProductExample;
import com.rikka.mall.service.ICartService;
import com.rikka.mall.vo.CartProductVO;
import com.rikka.mall.vo.CartVO;
import com.rikka.mall.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Yuno
 * @time 6:56 PM 6/7/2023
 */

@Service
public class CartServiceImpl implements ICartService {

    private static final String REDIS_CART_KEY_FORMAT = "cart_%d";

    @Autowired
    private StringRedisTemplate srt;

    @Autowired
    private MallProductMapper mpm;

    private Gson gson = new Gson();

    /**
     * 默认每次只添加1件商品
     */
    @Override
    //@SuppressWarnings("all")
    public ResponseVO<CartVO> add(Integer uid, CartAddForm caf) {
        // 默认每次添加的数量为1
        // TODO 更好的, 为什么不直接默认抽取为 1 呢 ?
        int QUANTITY = 1;

        // 检查商
        MallProduct product = mpm.selectByPrimaryKey(caf.getProductId());
        // 商品是否存在
        if (product == null) return ResponseVO.error(ResponseStatusEnum.PRODUCT_NOT_EXIST);
        //商品状态是否在售
        if (product.getStatus().equals(ProductStatusEnum.TAKE_OFF) || product.getStatus().equals(ProductStatusEnum.DELETE))
            return ResponseVO.error(ResponseStatusEnum.PRODUCT_OFF_SALE_OR_DELETE);
        // 商品数量是否超过1
        if (product.getStock() < 1) return ResponseVO.error(ResponseStatusEnum.PRODUCT_STOCK_ERROR);

        // 奖商品项信息写入到Redis中
        /*
        1. 获取
        2. 覆盖
         */
        CartProduct store;
        HashOperations<String, String, String> ops = srt.opsForHash();
        String pre = ops.get(String.format(REDIS_CART_KEY_FORMAT, uid), caf.getProductId().toString());
        if (pre != null) {
            CartProduct cp = gson.fromJson(pre, CartProduct.class);
            cp.setQuantity(cp.getQuantity() + QUANTITY);
            cp.setProductSelected(caf.getSelected());
            store = cp;
        } else {
            // TODO 此处的 selected 字段有什么用呢?
            store = new CartProduct(caf.getProductId(), QUANTITY, caf.getSelected());
        }
        String vJson = gson.toJson(store);
        ops.put(String.format(REDIS_CART_KEY_FORMAT, uid), caf.getProductId().toString(), vJson);

        return showAll(uid);
    }


    @Override
    public ResponseVO<CartVO> showAll(Integer uid) {
        HashOperations<String, String, String> ops = srt.opsForHash();
        Map<String, String> entries = ops.entries(String.format(REDIS_CART_KEY_FORMAT, uid));

        List<CartProductVO> clist = new ArrayList<>();
        Boolean cBool = true;
        BigDecimal cSum = BigDecimal.ZERO;
        Integer cQuan = 0;

        if (!entries.isEmpty()) {
            // select in db
            List<Integer> list = new ArrayList<>(entries.size());
            entries.forEach((k, v) -> list.add(Integer.parseInt(k)));
            MallProductExample mpe = new MallProductExample();
            mpe.createCriteria().andIdIn(list);
            List<MallProduct> products = mpm.selectByExample(mpe);

            // handle data
            // construct map for entries
            Map<Integer, CartProduct> redisMap = new HashMap<>(entries.size());
            entries.forEach((k, v) -> {
                int key = Integer.parseInt(k);
                CartProduct val = gson.fromJson(v, CartProduct.class);
                redisMap.put(key, val);
            });

            // construct a cartProductVO
            for (MallProduct product : products) {
                CartProduct cartProduct = redisMap.get(product.getId());
                CartProductVO cpvo = new CartProductVO(product.getId(), cartProduct.getQuantity(), product.getName(), product.getSubtitle(), product.getMainImage(), product.getPrice(), product.getStatus(), product.getPrice().multiply(BigDecimal.valueOf(cartProduct.getQuantity())), product.getStock(), cartProduct.getProductSelected());

                clist.add(cpvo);
                cQuan += cpvo.getQuantity();
                if (cartProduct.getProductSelected()) {
                    cSum = cSum.add(cpvo.getProductTotalPrice());
                } else {
                    cBool = false;
                }
            }

        }

        CartVO cvo = new CartVO(clist, cBool, cSum, cQuan);
        return ResponseVO.success(cvo);
    }

    @Override
    public ResponseVO<CartVO> update(Integer uid, Integer productId, CartUpdateForm form) {
        if (form.getSelected() == null && form.getQuantity() == null) return showAll(uid);

        HashOperations<String, String, String> ops = srt.opsForHash();
        String jsonProduct = ops.get(String.format(REDIS_CART_KEY_FORMAT, uid), productId.toString());
        if (jsonProduct == null) {
            return ResponseVO.error(ResponseStatusEnum.CART_PRODUCT_NOT_EXIST);
        }

        CartProduct cartProduct = gson.fromJson(jsonProduct, CartProduct.class);
        if (form.getQuantity() != null && form.getQuantity() >= 0)
            cartProduct.setQuantity(form.getQuantity());
        if (form.getSelected() != null) cartProduct.setProductSelected(form.getSelected());
        ops.put(String.format(REDIS_CART_KEY_FORMAT, uid), productId.toString(), gson.toJson(cartProduct));

        return showAll(uid);
    }

    @Override
    public ResponseVO<CartVO> delete(Integer uid, Integer productId) {
        HashOperations<String, String, String> ops = srt.opsForHash();
        Boolean check = ops.hasKey(String.format(REDIS_CART_KEY_FORMAT, uid), productId.toString());
        if (!check) return ResponseVO.error(ResponseStatusEnum.CART_PRODUCT_NOT_EXIST);

        srt.opsForHash().delete(String.format(REDIS_CART_KEY_FORMAT, uid), productId.toString());
        return showAll(uid);
    }

    @Override
    public ResponseVO<CartVO> selectAll(Integer uid) {
        return select(uid, true);
    }

    @Override
    public ResponseVO<CartVO> unSelectAll(Integer uid) {
        return select(uid, false);
    }

    private ResponseVO<CartVO> select(Integer uid, Boolean selected) {
        HashOperations<String, String, String> ops = srt.opsForHash();
        String key = String.format(REDIS_CART_KEY_FORMAT, uid);
        Map<String, String> entries = ops.entries(key);
        entries.forEach((k, v) -> {
            CartProduct cartProduct = gson.fromJson(v, CartProduct.class);
            cartProduct.setProductSelected(selected);
            ops.put(key, k, gson.toJson(cartProduct));
        });

        return showAll(uid);
    }

    @Override
    public ResponseVO<Integer> sum(Integer uid) {
        HashOperations<String, String, String> ops = srt.opsForHash();
        String key = String.format(REDIS_CART_KEY_FORMAT, uid);
        Map<String, String> entries = ops.entries(key);
        int cnt = 0;
        for (String v : entries.values()) {
            CartProduct cartProduct = gson.fromJson(v, CartProduct.class);
            cnt += cartProduct.getQuantity();
        }

        return ResponseVO.success(cnt);
    }

    @Override
    public List<CartProduct> listForCart(Integer uid) {
        HashOperations<String, String, String> ops = srt.opsForHash();
        String key = String.format(REDIS_CART_KEY_FORMAT, uid);
        Map<String, String> entries = ops.entries(key);

        List<CartProduct> ans = new ArrayList<>(entries.size());
        for (String v : entries.values()) {
            CartProduct cartProduct = gson.fromJson(v, CartProduct.class);
            ans.add(cartProduct);
        }

        return ans;
    }
}
