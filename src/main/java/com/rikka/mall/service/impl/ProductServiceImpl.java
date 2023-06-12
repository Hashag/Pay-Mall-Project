package com.rikka.mall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rikka.mall.consts.MallConst;
import com.rikka.mall.dao.MallProductMapper;
import com.rikka.mall.enums.ProductStatusEnum;
import com.rikka.mall.enums.ResponseStatusEnum;
import com.rikka.mall.pojo.MallProduct;
import com.rikka.mall.pojo.MallProductExample;
import com.rikka.mall.service.ICategoryService;
import com.rikka.mall.service.IProductService;
import com.rikka.mall.vo.MallCategoryVO;
import com.rikka.mall.vo.MallProductDetailVO;
import com.rikka.mall.vo.MallProductVO;
import com.rikka.mall.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Yuno
 * @time 3:19 PM 6/6/2023
 */

@Service
@Slf4j
public class ProductServiceImpl implements IProductService {


    @Autowired
    private MallProductMapper mpm;

    @Autowired
    private ICategoryService ics;


    @Override
    public ResponseVO<MallProductDetailVO> queryById(Integer pid) {
        MallProduct product = mpm.selectByPrimaryKey(pid);

        if (product == null) return ResponseVO.error(ResponseStatusEnum.PRODUCT_NOT_EXIST);

        if (ProductStatusEnum.TAKE_OFF.getCode().equals(product.getStatus()) ||
                ProductStatusEnum.DELETE.getCode().equals(product.getStatus())) {
            return ResponseVO.error(ResponseStatusEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }

        MallProductDetailVO vo = new MallProductDetailVO();
        BeanUtils.copyProperties(product, vo);
        return ResponseVO.success(vo);
    }

    @Override
    @SuppressWarnings("all")
    public ResponseVO<PageInfo> queryByCategoryId(Integer cId, Integer pageNo, Integer pageSize) {

        // 如果cId为null, 就返回所有类目的商品
        if (cId == null) cId = MallConst.CATEGORY_ROOT_ID;

        Set<Integer> set = ics.queryByCategoryId(cId);

        // 开启分页
        PageHelper.startPage(pageNo, pageSize);
        List<MallProduct> products;

        MallProductExample mpe = new MallProductExample();
        mpe.createCriteria().andCategoryIdIn(new ArrayList<>(set)).andStatusEqualTo(ProductStatusEnum.IN_STOKE.getCode());

        products = mpm.selectByExample(mpe);


        List<MallProductVO> productsVO = new ArrayList<>();
        for (MallProduct pro : products) {
            MallProductVO mpvo = new MallProductVO();
            BeanUtils.copyProperties(pro, mpvo);
            productsVO.add(mpvo);
        }

        PageInfo pageInfo = new PageInfo<>(products);
        pageInfo.setList(productsVO);

        log.info("the pageInfo is: {}", pageInfo);
        log.info("new pageInfo is: {}", new PageInfo<>());

        return ResponseVO.success(pageInfo);
    }
}
