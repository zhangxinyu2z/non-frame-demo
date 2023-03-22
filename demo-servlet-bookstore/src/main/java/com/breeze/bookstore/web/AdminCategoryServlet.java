package com.breeze.bookstore.web;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.breeze.bookstore.entity.EbookCategory;
import com.breeze.bookstore.exception.CategoryException;
import com.breeze.bookstore.service.EbookCategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-25 20:16
 */
@WebServlet("/AdminCategoryServlet")
public class AdminCategoryServlet extends BaseServlet {
    private EbookCategoryService ebookCategoryService = new EbookCategoryService();

    /**
     * 查询所有图书分类
     */
    public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("categoryList", ebookCategoryService.findAllCategory());
        return "f:/adminjsps/admin/category/list.jsp";
    }

    /**
     * 添加分类
     */
    public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EbookCategory ebookCategory = CommonUtils.toBean(request.getParameterMap(), EbookCategory.class);
        ebookCategory.setCid(CommonUtils.uuid());
        ebookCategoryService.add(ebookCategory);
        return findAll(request, response);
    }

    /**
     * 删除分类
     */
    public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        try {
            ebookCategoryService.delete(cid);
            return findAll(request,response);
        } catch (CategoryException e) {
            request.setAttribute("msg",e.getMessage());
            return "f:/adminjsps/msg.jsp";
        }
    }

    /**
     * 根据cid查询分类信息
     */
    public String findCategoryByCid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        EbookCategory ebookCategory = ebookCategoryService.findCategoryByCid(cid);
        request.setAttribute("category", ebookCategory);
        return "f:/adminjsps/admin/ebookCategory/mod.jsp";
    }

    /**
     * 修改指定图书分类信息
     */
    public String modifyCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EbookCategory ebookCategory = CommonUtils.toBean(request.getParameterMap(), EbookCategory.class);
        ebookCategoryService.modifyCategory(ebookCategory);
        return findAll(request,response);
    }

}
