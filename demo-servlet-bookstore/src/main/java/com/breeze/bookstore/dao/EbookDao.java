package com.breeze.bookstore.dao;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import com.breeze.bookstore.entity.EbookCategory;
import com.breeze.bookstore.entity.Ebook;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-24 23:15
 */
public class EbookDao {
    private QueryRunner queryRunner = new TxQueryRunner();

    /**
     * 查询所有书的信息。
     *
     * @return
     */
    public List<Ebook> findAllBook() {
        String sql = "select * from book where del=false";
        try {
            return queryRunner.query(sql, new BeanListHandler<Ebook>(Ebook.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * query book list by categeroy cid
     *
     * @param cid
     * @return
     */
    public List<Ebook> findBookByCategory(String cid) {
        String sql = "select * from book where cid=?";
        try {
            return queryRunner.query(sql, new BeanListHandler<Ebook>(Ebook.class), cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * query book info
     */
    public Ebook lookBookInfo(String bid) {
        String sql = "select * from book where bid=? and del=false";
        try {
            /*
             在图书后台系统中，查询单本图书，可以选择分类信息，使用BeanListHandler没有映射category信息，
             所以jsp页面通过cid判断是否相等来进行option的selected属性无效。
             */
            Map<String, Object> lineMap = queryRunner.query(sql, new MapHandler(), bid);
            EbookCategory ebookCategory = CommonUtils.toBean(lineMap, EbookCategory.class);
            Ebook ebook = CommonUtils.toBean(lineMap, Ebook.class);
            ebook.setEbookCategory(ebookCategory);
            return ebook;
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    /**
     * 查询指定分类下的图书数量
     *
     * @param cid
     * @return
     */
    public int getBookCountByCid(String cid) {
        String sql = "select count(*) from book where cid=? and del=false";
        try {
            Number count = (Number) queryRunner.query(sql, new ScalarHandler(), cid);
            return count.intValue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 保存添加的图书
     *
     * @param ebook
     */
    public void add(Ebook ebook) {
        String sql = "insert into book values (?,?,?,?,?,?)";
        Object[] params = {ebook.getBid(), ebook.getBname(), ebook.getPrice(), ebook.getAuthor(), ebook.getImage(),
                ebook.getEbookCategory().getCid()};
        try {
            queryRunner.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改图书信息
     *
     * @param ebook
     */
    public void edit(Ebook ebook) {
        String sql = "update book set bname=?,price=?,author=?,image=?,cid=? where bid=?";
        Object[] params = {ebook.getBname(), ebook.getPrice(), ebook.getAuthor(), ebook.getImage(), ebook.getEbookCategory().getCid(), ebook.getBid()};
        try {
            queryRunner.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除图书
     */
    public void delete(String bid) {
        String sql = "update book set del=true where bid=?";
        try {
            queryRunner.update(sql, bid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
