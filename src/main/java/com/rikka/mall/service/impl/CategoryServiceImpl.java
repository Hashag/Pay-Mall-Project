package com.rikka.mall.service.impl;

import com.rikka.mall.consts.MallConst;
import com.rikka.mall.dao.MallCategoryMapper;
import com.rikka.mall.enums.CategoryStatusEnum;
import com.rikka.mall.pojo.MallCategory;
import com.rikka.mall.pojo.MallCategoryExample;
import com.rikka.mall.service.ICategoryService;
import com.rikka.mall.vo.MallCategoryVO;
import com.rikka.mall.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Yuno
 * @time 11:40 AM 5/31/2023
 */
@Service
@Slf4j
public class CategoryServiceImpl implements ICategoryService {


    @Autowired
    private MallCategoryMapper mcm;

    // TODO 是否需要将所有类目数据封装为一个成员变量, 取决于数据量的大小, 目前还是不要封装为一个成员变量为妙


    @Override
    public Set<Integer> queryByCategoryId(Integer cId) {
        Set<Integer> ans = new HashSet<>();
        if (cId == null) return ans;
        Map<Integer, List<MallCategory>> map = classifyAllByParentIdToMap();
        // 仍然选择BFS
        LinkedList<Integer> queue = new LinkedList<>();
        queue.offer(cId);
        while (!queue.isEmpty()) {
            Integer poll = queue.poll();
            ans.add(poll);
            List<MallCategory> list = map.get(poll);
            if (list != null) {
                for (MallCategory cat : list) {
                    queue.offer(cat.getId());
                }
            }
        }

        return ans;
    }


    /**
     * 一次查詢出所有有效分類數據, 之後按parentId進行分類, 方便構建多級分類時查找數據
     */
    @Override
    public ResponseVO<List<MallCategoryVO>> queryAll() {
        Map<Integer, List<MallCategory>> map = classifyAllByParentIdToMap();
        return ResponseVO.success(queryAll(MallConst.CATEGORY_ROOT_ID, map));
    }

    /**
     * 查询出所有类目信息
     *
     * @return 按ParentId分类号的类目
     */
    private Map<Integer, List<MallCategory>> classifyAllByParentIdToMap() {
        MallCategoryExample example = new MallCategoryExample();
        example.createCriteria().andStatusEqualTo(CategoryStatusEnum.IN_USE.getCode());
        List<MallCategory> all = mcm.selectByExample(example);
        Map<Integer, List<MallCategory>> map = new HashMap<>();
        for (MallCategory category : all) {
            Integer parentId = category.getParentId();
            if (!map.containsKey(parentId)) {
                map.put(parentId, new ArrayList<>());
            }
            map.get(parentId).add(category);
        }
        return map;
    }

    /**
     * @param rootId 分類根id
     * @param map    所有有效的數據, 按parentId進行分類好
     * @return 構建好的多級目錄
     * 使用BFS來進行構建
     */
    private List<MallCategoryVO> queryAll(int rootId, Map<Integer, List<MallCategory>> map) {
        LinkedList<MallCategoryVO> queue = new LinkedList<>();
        MallCategoryVO root = new MallCategoryVO();
        root.setId(rootId);
        queue.offer(root);
        while (!queue.isEmpty()) {
            MallCategoryVO poll = queue.poll();
            List<MallCategory> categories = map.get(poll.getId());
            List<MallCategoryVO> sub;
            // if poll does have sub
            if (categories != null) {
                sub = new ArrayList<>(categories.size());
                for (MallCategory category : categories) {
                    MallCategoryVO mcvo = new MallCategoryVO();
                    BeanUtils.copyProperties(category, mcvo);
                    sub.add(mcvo);
                    // put to the queue, later find if it owns sub
                    queue.offer(mcvo);
                }
                // sort by field sortOrder 逆序
                sub.sort((e1, e2) -> -Integer.compare(e1.getSortOrder(), e2.getSortOrder()));
            } else {
                sub = new ArrayList<>();
            }
            // set sub, and make sure sub isn't null
            poll.setSubCategories(sub);
        }

        return root.getSubCategories();
    }

}
