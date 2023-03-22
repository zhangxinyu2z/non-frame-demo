package com.breeze.bookstore.dao;

import cn.itcast.jdbc.TxQueryRunner;
import com.breeze.bookstore.entity.EbookCategory;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-24 22:59
 */
public class EbookCategoryDao {
    private QueryRunner queryRunner = new TxQueryRunner();

    public List<EbookCategory> findAllCategory() {
        String sql = "select * from category";
        try {
            return queryRunner.query(sql, new BeanListHandler<EbookCategory>(EbookCategory.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加图书分类项目
     *
     * @param ebookCategory
     */
    public void add(EbookCategory ebookCategory) {
        String sql = "insert into category values (?,?)";
        try {
            queryRunner.update(sql, ebookCategory.getCid(), ebookCategory.getCname());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除指定分类
     */
    public void delete(String cid) {
        String sql = "delete from category where cid=?";
        try {
            queryRunner.update(sql, cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据cid查询分类信息
     */
    public EbookCategory findCategoryByCid(String cid) {
        String sql = "select * from category where cid=?";
        try {
            return queryRunner.query(sql, new BeanHandler<EbookCategory>(EbookCategory.class), cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改指定图书信息
     */
    public void modifyCategory(EbookCategory ebookCategory) {
        String sql = "update category set cname=? where cid=?";
        try {
            queryRunner.update(sql, ebookCategory.getCname(), ebookCategory.getCid());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
