package com.breeze.bookstore.service;

import com.breeze.bookstore.dao.EbookDao;
import com.breeze.bookstore.dao.EbookCategoryDao;
import com.breeze.bookstore.entity.EbookCategory;
import com.breeze.bookstore.exception.CategoryException;

import java.util.List;

/**
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-24 22:58
 */
public class EbookCategoryService {
    private EbookCategoryDao ebookCategoryDao = new EbookCategoryDao();
    private EbookDao ebookDao = new EbookDao();

    /**
     * 查询所有分类
     */
    public List<EbookCategory> findAllCategory() {
        return ebookCategoryDao.findAllCategory();
    }

    /**
     * 添加分类
     */
    public void add(EbookCategory ebookCategory) {
        ebookCategoryDao.add(ebookCategory);
    }

    /**
     * 删除指定分类
     */
    public void delete(String cid) throws CategoryException {
        int count = ebookDao.getBookCountByCid(cid);
        if(count > 0) {
            throw new CategoryException("当前分类下还有图书，不能删除！");
        }
        ebookCategoryDao.delete(cid);
    }

    /**
     * 查询指定图书类别信息
     */
    public EbookCategory findCategoryByCid(String cid) {
        return ebookCategoryDao.findCategoryByCid(cid);
    }

    /**
     * 修改图书类别信息
     */
    public void modifyCategory(EbookCategory ebookCategory) {
        ebookCategoryDao.modifyCategory(ebookCategory);
    }
}
