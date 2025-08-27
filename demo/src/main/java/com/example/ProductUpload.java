package com.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/product/upload")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1MB 이상이면 임시파일로
    maxFileSize = 1024 * 1024 * 10,  // 최대 10MB
    maxRequestSize = 1024 * 1024 * 50 // 최대 50MB
)
public class ProductUpload extends HttpServlet {
    private List<Product> productList = new ArrayList<>();
    private final String uploadDir = "C:/myapp/uploads/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Object loginUser = req.getSession().getAttribute("loginUser");

        if (!(loginUser instanceof Company)) {
            resp.setContentType("text/html;charset=UTF-8");
            WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
            ctx.setVariable("errorMessage", "로그인이 필요합니다.");

            TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
            templateEngine.process("login", ctx, resp.getWriter());
            return;
        }

        Company company = (Company) loginUser;
        resp.setContentType("text/html;charset=UTF-8");
        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());

        ctx.setVariable("company", company);

        Product emptyProduct = new Product();
        emptyProduct.setCount(1);
        ctx.setVariable("product", emptyProduct);

        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        templateEngine.process("product", ctx, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String productName = req.getParameter("productName");
        int price = Integer.parseInt(req.getParameter("price"));
        int count = Integer.parseInt(req.getParameter("count"));

        Part filePart = req.getPart("profilePath");
        String fileName = null;

        if (filePart != null && filePart.getSize() > 0) {
            // 헤더에서 파일명 직접 추출
            String contentDisposition = filePart.getHeader("Content-Disposition");
            String originalFileName = extractFileName(contentDisposition);

            String extension = "";
            int dotIndex = originalFileName.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = originalFileName.substring(dotIndex);
            }

            fileName = UUID.randomUUID().toString() + extension;

            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            filePart.write(uploadDir + fileName);
        }

        Product product = new Product(productName, price, count, fileName);
        productList.add(product);

        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        templateEngine.process("index", ctx, resp.getWriter());
    }

    // Content-Disposition 헤더에서 파일명 추출
    private String extractFileName(String contentDisposition) {
        if (contentDisposition == null) return "";
        for (String cdPart : contentDisposition.split(";")) {
            cdPart = cdPart.trim();
            if (cdPart.startsWith("filename")) {
                String fileName = cdPart.substring(cdPart.indexOf('=') + 1).trim().replace("\"", "");
                // IE에서 전체 경로가 올 수 있으므로 마지막 '\' 이후만 취함
                return fileName.substring(fileName.lastIndexOf('\\') + 1);
            }
        }
        return "";
    }
}
