package com.laporndan.controller;

import com.laporndan.config.HibernateUtil;
import com.laporndan.model.Laporan;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.hibernate.Session;
import org.hibernate.Transaction;

@WebServlet(name = "ProsesLaporan", urlPatterns = {"/ProsesLaporan"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,        // 10MB
    maxRequestSize = 1024 * 1024 * 50      // 50MB
)
public class ProsesLaporan extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String namaPelapor = request.getParameter("nama_pelapor");
        String tanggal = request.getParameter("tanggal");
        String waktu = request.getParameter("waktu");
        String lokasi = request.getParameter("lokasi");
        String kedalamanStr = request.getParameter("kedalaman");
        int kedalaman = Integer.parseInt(kedalamanStr);
        
        Part filePart = request.getPart("foto");
        String fileName = extractFileName(filePart);
        
        // 1. Simpan permanen ke folder source OneDrive (Aman dari Clean and Build)
        String sourcePath = "C:\\Users\\itoto\\OneDrive\\Documents\\NetBeansProjects\\LaporNdan\\src\\main\\webapp\\assets\\uploads";
        File sourceDir = new File(sourcePath);
        if (!sourceDir.exists()) {
            sourceDir.mkdirs();
        }
        File sourceFile = new File(sourceDir, fileName);
        filePart.write(sourceFile.getAbsolutePath());

        // 2. Salin juga secara instan ke folder deployment aktif Tomcat supaya langsung muncul
        String runtimePath = getServletContext().getRealPath("") + File.separator + "assets" + File.separator + "uploads";
        File runtimeDir = new File(runtimePath);
        if (!runtimeDir.exists()) {
            runtimeDir.mkdirs();
        }
        File runtimeFile = new File(runtimeDir, fileName);
        Files.copy(sourceFile.toPath(), runtimeFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // 3. Simpan ke Database via Hibernate
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        
        try {
            tx = session.beginTransaction();
            
            Laporan laporan = new Laporan();
            laporan.setNamaPelapor(namaPelapor);
            laporan.setTanggal(tanggal);
            laporan.setWaktu(waktu);
            laporan.setLokasi(lokasi);
            laporan.setKedalaman(kedalaman);
            laporan.setFoto(fileName);
            laporan.setStatus("Belum Ditangani");
            
            session.persist(laporan);
            tx.commit();
            
            response.sendRedirect("index.jsp");
            
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            try (PrintWriter out = response.getWriter()) {
                out.println("<h3>Gagal menyimpan ke database via Hibernate: " + e.getMessage() + "</h3>");
            }
        } finally {
            session.close();
        }
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}