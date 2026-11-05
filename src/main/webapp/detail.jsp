<%@ page import="java.sql.*" %>
<%@ page import="com.laporndan.config.Koneksi" %>
<%
    String idStr = request.getParameter("id");
    int id = 0;
    
    String lokasi = "-";
    String tanggal = "-";
    String waktu = "-";
    int kedalaman = 0;
    String foto = "";
    String status = "Belum Ditangani";
    
    if (idStr != null && !idStr.isEmpty()) {
        try {
            id = Integer.parseInt(idStr);
            Connection conn = Koneksi.getKoneksi();
            String sql = "SELECT * FROM laporan WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                lokasi = rs.getString("lokasi");
                tanggal = rs.getString("tanggal");
                waktu = rs.getString("waktu");
                kedalaman = rs.getInt("kedalaman");
                foto = rs.getString("foto");
                status = rs.getString("status");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detail Laporan - Lapor Ndan</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style> body { background-color: #f4f6f9; } </style>
</head>
<body>

    <nav class="navbar navbar-dark bg-primary shadow-sm mb-4">
        <div class="container">
            <a class="navbar-brand fw-bold" href="index.jsp">Kembali ke Halaman Utama</a>
        </div>
    </nav>

    <div class="container" style="max-width: 800px;">
        <div class="card shadow-sm mb-4">
            <div class="card-body">
                <span class="badge bg-warning text-dark mb-2">Status: <%= status %></span>
                <h4 class="fw-bold">Lokasi: <%= lokasi %></h4>
                <p class="text-muted mb-2">Ditemukan pada: <%= tanggal %> - Pukul <%= waktu %> WIB | Kedalaman: <%= kedalaman %> cm</p>
                
                <% if (foto != null && !foto.isEmpty()) { %>
                    <img src="assets/uploads/<%= foto %>" class="img-fluid rounded mb-4" alt="Foto Lubang">
                <% } else { %>
                    <img src="https://via.placeholder.com/700x350?text=Tidak+Ada+Foto" class="img-fluid rounded mb-4" alt="Foto Kosong">
                <% } %>

                <!-- BAGIAN DISKUSI / KOMENTAR -->
                <h5 class="fw-bold border-bottom pb-2 mt-4">Diskusi & Log Komentar</h5>

                <!-- Load Komentar dari Database -->
                <%
                    try {
                        Connection conn = Koneksi.getKoneksi();
                        String sqlKomentar = "SELECT * FROM komentar WHERE id_laporan = ? ORDER BY tanggal_komentar DESC";
                        PreparedStatement pstmtK = conn.prepareStatement(sqlKomentar);
                        pstmtK.setInt(1, id);
                        ResultSet rsK = pstmtK.executeQuery();
                        
                        boolean adaKomentar = false;
                        while(rsK.next()) {
                            adaKomentar = true;
                            String namaPengguna = rsK.getString("nama_pengguna");
                            String isiKomentar = rsK.getString("isi_komentar");
                            String fotoBukti = rsK.getString("foto_bukti");
                            String tglKomentar = rsK.getString("tanggal_komentar");
                %>
                            <div class="bg-light p-3 rounded mb-3">
                                <strong class="d-block text-primary"><%= (namaPengguna != null && !namaPengguna.isEmpty()) ? namaPengguna : "Warga / Pelapor" %></strong>
                                <p class="mb-1 text-dark"><%= isiKomentar %></p>
                                <% if(fotoBukti != null && !fotoBukti.isEmpty()) { %>
                                    <div class="mb-2">
                                        <img src="assets/uploads/<%= fotoBukti %>" class="img-thumbnail" style="max-height: 150px;" alt="Bukti Perbaikan">
                                    </div>
                                <% } %>
                                <small class="text-muted"><%= tglKomentar %></small>
                            </div>
                <% 
                        }
                        if(!adaKomentar) {
                            out.println("<p class='text-muted italic'>Belum ada komentar atau update pada laporan ini.</p>");
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                %>

                <!-- Form Tambah Komentar -->
                <div class="mt-4 pt-3 border-top">
                    <h6>Tambah Komentar / Bukti Perbaikan:</h6>
                    <form action="ProsesKomentar" method="POST" enctype="multipart/form-data">
                        <input type="hidden" name="id_laporan" value="<%= id %>">
                        <div class="mb-2">
                            <label class="form-label small">Nama Anda:</label>
                            <input type="text" name="nama_pengguna" class="form-control form-control-sm" placeholder="Masukkan nama kamu..." required>
                        </div>
                        <div class="mb-2">
                            <label class="form-label small">Komentar / Update:</label>
                            <textarea name="isi_komentar" class="form-control" rows="3" placeholder="Tulis komentar, pertanyaan, atau update lokasi..." required></textarea>
                        </div>
                        <div class="mb-3">
                            <label class="form-label small">Unggah Foto Bukti Perbaikan (Opsional):</label>
                            <input type="file" name="foto_bukti" class="form-control" accept="image/*">
                        </div>
                        <button type="submit" class="btn btn-primary btn-sm">Kirim Komentar</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

</body>
</html>