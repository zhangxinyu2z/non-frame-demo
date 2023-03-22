package com.breeze.bookstore.web;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.breeze.bookstore.entity.Ebook;
import com.breeze.bookstore.entity.EbookCategory;
import com.breeze.bookstore.service.EbookService;
import com.breeze.bookstore.service.EbookCategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-25 21:47
 */
@WebServlet("/AdminBookServlet")
public class AdminBookServlet extends BaseServlet {
    private EbookService ebookService = new EbookService();
    private EbookCategoryService ebookCategoryService = new EbookCategoryService();

    /**
     * 查看所有图书信息
     */
    public String findAllBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Ebook> ebookList = ebookService.findAllBook();
        request.setAttribute("bookList", ebookList);
        return "f:/adminjsps/admin/book/list.jsp";
    }

    /**
     * 查看图书详情
     */
    public String lookBookInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bid = request.getParameter("bid");
        Ebook ebook = ebookService.lookBookInfo(bid);
        request.setAttribute("book", ebook);
        request.setAttribute("categoryList", ebookCategoryService.findAllCategory());
        return "f:/adminjsps/admin/ebook/desc.jsp";
    }


    /**
     * 添加图书页面，显示所有分类
     */
    public String addCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<EbookCategory> ebookCategoryList = ebookCategoryService.findAllCategory();
        request.setAttribute("categoryList", ebookCategoryList);
        return "f:/adminjsps/admin/book/add.jsp";
    }

    /**
     * 修改图书信息
     */
    public String edit(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        Ebook ebook = CommonUtils.toBean(request.getParameterMap(), Ebook.class);
        EbookCategory ebookCategory = CommonUtils.toBean(request.getParameterMap(), EbookCategory.class);
        ebook.setEbookCategory(ebookCategory);
        ebookService.edit(ebook);
        return findAllBook(request,response);
    }

    /**
     * 删除指定图书
     * 删除图书不是真删除，而是修改图书del状态
     */
    public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        String bid = request.getParameter("bid");
        ebookService.delete(bid);
        return findAllBook(request,response);
    }


}
