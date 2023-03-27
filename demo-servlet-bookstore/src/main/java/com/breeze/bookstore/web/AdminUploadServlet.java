package com.breeze.bookstore.web;

import cn.itcast.commons.CommonUtils;
import com.breeze.bookstore.entity.EbookCategory;
import com.breeze.bookstore.entity.Ebook;
import com.breeze.bookstore.service.EbookService;
import com.breeze.bookstore.service.EbookCategoryService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 上传文件的servlet
 *
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-25 21:47
 */
@WebServlet("/upload")
public class AdminUploadServlet extends HttpServlet {
    private EbookCategoryService ebookCategoryService = new EbookCategoryService();
    private EbookService ebookService = new EbookService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");

        /*
        1、将表单数据封装到Book中
         */
        // 创建工厂，设置文件缓存位置和大小
        DiskFileItemFactory factory = new DiskFileItemFactory(15 * 1024, new File("D:/tmp"));
        // 得到解析器
        ServletFileUpload sfu = new ServletFileUpload(factory);
        // 设置单个文件大小为20KB,如果超出20kb，会抛出FileUploadBase.FileSizeLimitExceededException异常
        sfu.setFileSizeMax(1024 * 1024);
        try {
            // 使用解析器去解析request对象，得到List<FileItem>，每一个fileItem对应一个提交的输入框数据
            List<FileItem> fileItemList = sfu.parseRequest(req);
            // 先把所有的普通表单字段数据先封装到Map中，方便tobean映射
            Map<String, String> map = new HashMap<String, String>();
            for (FileItem fileItem : fileItemList) {
                // 如果是一个简单的表单数据
                if (fileItem.isFormField()) {
                    map.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
                }
            }
            Ebook book = CommonUtils.toBean(map, Ebook.class);
            book.setBid(CommonUtils.uuid());
            // 为了存储cid
            EbookCategory ebookCategory = CommonUtils.toBean(map, EbookCategory.class);
            book.setEbookCategory(ebookCategory);

            /*
             * 2、保存上传的文件的到服务器磁盘根目录
             */
            // 上传文件保存到服务器磁盘的目录
            String savePath = req.getServletContext().getRealPath("/book_img");
            // 得到上传文件名字
            String fileName = CommonUtils.uuid() + "_" + fileItemList.get(1).getName();

            // 对上传图片进行格式校验
            if (!fileName.toLowerCase().endsWith(".jpg")) {
                req.setAttribute("msg", "只能上传.jpg结尾的图片");
                req.setAttribute("categoryList", ebookCategoryService.findAllCategory());
                req.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(req,resp);
                return;
            }
            // 文件路径
            File destFile = new File(savePath, fileName);
            // 保存上传文件到服务器磁盘
            fileItemList.get(1).write(destFile);

            // 3、设置book对象的image属性，为相对路径
            book.setImage("book_img/" + fileName);

            // 4、把book保存到数据库
            ebookService.add(book);

            // 图片分辨率校验
            Image image = new ImageIcon(destFile.getAbsolutePath()).getImage();
            if(image.getWidth(null) > 1000 || image.getHeight(null) > 1000) {
                destFile.delete();//删除这个文件！
                req.setAttribute("msg", "您上传的图片尺寸超出了200 * 200！");
                req.setAttribute("categoryList", ebookCategoryService.findAllCategory());
                req.getRequestDispatcher("/adminjsps/admin/book/add.jsp")
                        .forward(req, resp);
                return;
            }

            //5、返回到图书列表
            req.getRequestDispatcher("/AdminBookServlet?method=findAllBook").forward(req,resp);
        } catch (Exception e) {
            if(e instanceof FileUploadBase.FileSizeLimitExceededException) {
                req.setAttribute("msg", "您上传的文件超出了20KB");
                req.setAttribute("categoryList", ebookCategoryService.findAllCategory());
                req.getRequestDispatcher("/adminjsps/admin/book/add.jsp")
                        .forward(req, resp);
            }
        }

    }
}
