package com.breeze.bookstore.service;

import com.breeze.bookstore.dao.EbookDao;
import com.breeze.bookstore.entity.Ebook;

import java.util.List;

/**
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-24 23:16
 */
public class EbookService {
    private EbookDao ebookDao =new EbookDao();

    /**
     * 查询所有书籍信息
     */
    public List<Ebook> findAllBook() {
        return ebookDao.findAllBook();
    }

    /**
     * query bookList info by category cid
     * @param cid
     * @return
     */
    public List<Ebook> findBookByCategory(String cid) {
        return ebookDao.findBookByCategory(cid);
    }

    /**
     * query book info by bid
     * @param bid
     * @return
     */
    public Ebook lookBookInfo(String bid) {
        return ebookDao.lookBookInfo(bid);
    }

    public void add(Ebook ebook) {
        ebookDao.add(ebook);
    }

    public void edit(Ebook ebook) {
        ebookDao.edit(ebook);
    }

    public void delete(String bid) {
        ebookDao.delete(bid);
    }
}
