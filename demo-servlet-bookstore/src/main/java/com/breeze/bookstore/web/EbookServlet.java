package com.breeze.bookstore.web;

import cn.itcast.servlet.BaseServlet;
import com.breeze.bookstore.entity.Ebook;
import com.breeze.bookstore.service.EbookService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-24 23:16
 */
@WebServlet("/ebook")
public class EbookServlet extends BaseServlet {
    private EbookService ebookService = new EbookService();

    /**
     * query all book info list
     */
    public String findAllBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Ebook> bookList = ebookService.findAllBook();
        request.setAttribute("bookList", bookList);
        return "f:/jsps/book/list.jsp";
    }

    /**
     * query book list infos by category id
     */
    public String findBookByCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        List<Ebook> bookList = ebookService.findBookByCategory(cid);
        request.setAttribute("bookList", bookList);
        return "f:/jsps/book/list.jsp";
    }

    /**
     *  look single booko info
     */
    public String lookBookInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bid = request.getParameter("bid");
        Ebook book= ebookService.lookBookInfo(bid);
        request.setAttribute("book", book);
        return "f:/jsps/book/desc.jsp";
    }

}
