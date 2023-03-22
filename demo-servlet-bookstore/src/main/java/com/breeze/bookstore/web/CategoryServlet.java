package com.breeze.bookstore.web;

import cn.itcast.servlet.BaseServlet;
import com.breeze.bookstore.entity.EbookCategory;
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
 * @date created in 2021-05-24 22:59
 */
@WebServlet("/CategoryServlet")
public class CategoryServlet extends BaseServlet {
    private EbookCategoryService categoryService = new EbookCategoryService();

    /**
     * 查询所有分裂信息
     */
    public String findAllCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<EbookCategory> categories = categoryService.findAllCategory();
        request.setAttribute("categoryList",categories);
        return "f:/jsps/left.jsp";
    }

}
