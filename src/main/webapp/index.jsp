<%@ page import="java.util.List" %>
<%@ page import="com.laporndan.dao.LaporanDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lapor Ndan - Forum Laporan Jalan Berlubang</title>
    <!-- Menggunakan CDN Bootstrap 5 agar tampilan langsung rapi -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f6f9; }
        .card-feed { margin-bottom: 20px; }
        /* Modal Popup Styling Sederhana */
        .modal-login { display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 1000; }
        .modal-content-custom { background: #fff; width: 350px; margin: 100px auto; padding: 20px; border-radius: 8px; }
    </style>
</head>
<body>

    <!-- 1. HEADER & NAVIGATION (User_Interface) -->
    <nav class="navbar navbar-dark bg-primary shadow-sm mb-4">
        <div class="container">
            <a class="navbar-brand fw-bold" href="#">Lapor Ndan</a>
            <!-- Tombol untuk memunculkan Modal Login Admin -->
            <button class="btn btn-light btn-sm fw-semibold" onclick="bukaModalLogin()">Login Admin</button>
        </div>
    </nav>

    <div class="container" style="max-width: 800px;">

        <!-- 2. FORM INPUT LAPORAN (CRUD_Operations: Tambah Laporan) -->
        <div class="card shadow-sm mb-4">
            <div class="card-header bg-white fw-bold">
                Lapor temuan jalan berlubang
            </div>
            <div class="card-body">
                <form action="ProsesLaporan" method="POST" enctype="multipart/form-data">
                    <div class="row g-3">
                        <!-- Input Nama Pelapor Ditambahkan di Sini -->
                        <div class="col-12">
                            <label class="form-label">Nama Pelapor</label>
                            <input type="text" name="nama_pelapor" class="form-control" placeholder="Masukkan nama lengkap kamu..." required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Tanggal Ditemukan</label>
                            <input type="date" name="tanggal" class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Waktu Ditemukan</label>
                            <input type="time" name="waktu" class="form-control" required>
                        </div>
                        <div class="col-12">
                            <label class="form-label">Lokasi / Patokan Jalan</label>
                            <input type="text" name="lokasi" class="form-control" placeholder="Contoh: Jl. Raya Pantura dekat masjid berwarna hijau persis depan perempatan" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Perkiraan Kedalaman Lubang (cm)</label>
                            <input type="number" name="kedalaman" class="form-control" placeholder="__cm" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Foto Kerusakan Jalan</label>
                            <input type="file" name="foto" class="form-control" accept="image/*" required>
                        </div>
                        <div class="col-12 text-end mt-3">
                            <button type="submit" class="btn btn-primary px-4 fw-bold">Kirim Laporan</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <h5 class="fw-bold mb-3">Feed Laporan Terkini</h5>
        <%
            List<String[]> listLaporan = LaporanDao.getAllLaporan();
            if (listLaporan.isEmpty()) {
        %>
            <div class="alert alert-info text-center">Belum ada laporan jalan berlubang yang dikirim.</div>
        <%
            } else {
                for (String[] lap : listLaporan) {
                    String id = lap[0];
                    // Sesuaikan indeks array jika nama_pelapor ditambahkan di DAO:
                    // Contoh: lap[1] = nama_pelapor, lap[2] = tanggal, dst. (Sesuaikan dengan urutan SELECT di LaporanDao kamu)
                    String tanggal = lap[1];
                    String waktu = lap[2];
                    String lokasi = lap[3];
                    String kedalaman = lap[4];
                    String foto = lap[5];
                    String status = lap[6];
                    
                    String badgeClass = status.equals("Selesai") ? "bg-success" : "bg-warning text-dark";
        %>
            <div class="card card-feed shadow-sm">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <span class="badge <%= badgeClass %>">Status: <%= status %></span>
                        <small class="text-muted"><%= tanggal %> - <%= waktu %> WIB</small>
                    </div>
                    <h6 class="fw-bold text-primary mb-1">Lokasi: <%= lokasi %></h6>
                    <p class="mb-2 text-muted">Perkiraan kedalaman lubang: <b><%= kedalaman %> cm</b></p>

                    <img src="assets/uploads/<%= foto %>" class="img-fluid rounded mb-3" alt="Foto Lubang" onerror="this.src='https://via.placeholder.com/700x350?text=Foto+Tidak+Ditemukan'">

                    <div class="d-flex justify-content-between align-items-center border-top pt-2">
                        <a href="detail.jsp?id=<%= id %>" class="btn btn-outline-primary btn-sm">Lihat Detail & Komentar</a>

                        <div class="admin-only-buttons d-none">
                            <a href="AksiAdmin?action=selesai&id=<%= id %>" class="btn btn-success btn-sm me-1">Mark Completed</a>
                            <a href="AksiAdmin?action=hapus&id=<%= id %>" class="btn btn-danger btn-sm" onclick="return confirm('Yakin ingin menghapus laporan ini?')">Hapus</a>
                        </div>
                    </div>
                </div>
            </div>
        <%
                }
            }
        %>

    </div>

    <div id="modalLogin" class="modal-login">
        <div class="modal-content-custom shadow">
            <h5 class="fw-bold mb-3 text-center">Login Admin</h5>
            <form>
                <div class="mb-3">
                    <label class="form-label">Username Admin</label>
                    <input type="text" class="form-control" placeholder="Masukkan username">
                </div>
                <div class="mb-3">
                    <label class="form-label">Password</label>
                    <input type="password" class="form-control" placeholder="Masukkan password">
                </div>
                <div class="d-flex justify-content-between mt-4">
                    <button type="button" class="btn btn-secondary btn-sm" onclick="tutupModalLogin()">Batal</button>
                    <button type="button" class="btn btn-primary btn-sm" onclick="simulasiLoginAdmin()">Login</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function bukaModalLogin() {
            document.getElementById('modalLogin').style.display = 'block';
        }

        function tutupModalLogin() {
            document.getElementById('modalLogin').style.display = 'none';
        }

        function simulasiLoginAdmin() {
            alert("Login Admin Berhasil! (Simulasi Mode Admin Aktif)");
            tutupModalLogin();
            let adminBtns = document.querySelectorAll('.admin-only-buttons');
            adminBtns.forEach(btn => btn.classList.remove('d-none'));
        }
    </script>
</body>
</html>