<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Boarding House</title>
    
    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
</head>

<body>

<!-- NAVBAR -->
<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold" href="#">BOARDING HOUSE</a>

        <ul class="navbar-nav ms-auto">
            <li class="nav-item"><a class="nav-link" href="#">Home</a></li>
            <li class="nav-item"><a class="nav-link" href="#">Rooms</a></li>
            <li class="nav-item"><a class="nav-link" href="#">Services</a></li>
            <li class="nav-item"><a class="nav-link" href="#">Contact</a></li>
            <li class="nav-item ms-3">
                <a href="login.jsp" class="btn btn-outline-dark">Login</a>
            </li>
        </ul>
    </div>
</nav>

<!-- ROOM SECTION -->
<section class="container mt-5">

    <div class="text-center mb-5">
        <h2 class="fw-bold">Our Available Rooms</h2>
        <p class="text-muted">Find your perfect space</p>
    </div>

    <div class="row g-4">

        <!-- Room 1 -->
        <div class="col-md-4">
            <div class="room-card">
                <img src="https://source.unsplash.com/600x400/?bedroom" class="img-fluid">
                <div class="room-info">
                    <h4>Room A1</h4>
                    <p class="text-muted">Garden View</p>
                    <div class="d-flex justify-content-between align-items-center mt-3">
                        <span class="price">2,500,000 VND</span>
                        <a href="#" class="btn book-btn">Book Now</a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Room 2 -->
        <div class="col-md-4">
            <div class="room-card">
                <img src="https://source.unsplash.com/600x400/?apartment" class="img-fluid">
                <div class="room-info">
                    <h4>Room B2</h4>
                    <p class="text-muted">Balcony View</p>
                    <div class="d-flex justify-content-between align-items-center mt-3">
                        <span class="price">3,000,000 VND</span>
                        <a href="#" class="btn book-btn">Book Now</a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Room 3 -->
        <div class="col-md-4">
            <div class="room-card">
                <img src="https://source.unsplash.com/600x400/?modern-room" class="img-fluid">
                <div class="room-info">
                    <h4>Room C3</h4>
                    <p class="text-muted">City View</p>
                    <div class="d-flex justify-content-between align-items-center mt-3">
                        <span class="price">2,800,000 VND</span>
                        <a href="#" class="btn book-btn">Book Now</a>
                    </div>
                </div>
            </div>
        </div>

    </div>
</section>

<!-- FOOTER -->
<footer class="bg-dark text-white text-center p-3 mt-5">
    © 2026 Boarding House Management
</footer>

</body>
</html>