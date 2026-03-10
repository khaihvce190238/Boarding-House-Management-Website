<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Customer Dashboard</title>

        <!-- Bootstrap 5 -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

        <style>
            body {
                background-color: #f4f6f9;
            }
            .dashboard-card {
                border-radius: 15px;
                transition: 0.3s;
            }
            .dashboard-card:hover {
                transform: translateY(-5px);
            }
            .feature-card {
                border-radius: 15px;
                transition: 0.3s;
                cursor: pointer;
            }
            .feature-card:hover {
                background-color: #f1f1f1;
                transform: translateY(-5px);
            }
        </style>
    </head>

    <body>

        <div class="container mt-4">

            <!-- HEADER -->
            <div class="d-flex justify-content-between align-items-center">
                <h3>
                    Welcome, ${sessionScope.user.fullName}
                </h3>
                <a href="${pageContext.request.contextPath}/auth?action=logout"
                   class="btn btn-danger btn-sm">
                    Logout
                </a>
            </div>

            <hr>

            <!-- QUICK STATS -->
            <div class="row mt-4">

                <div class="col-md-4 mb-3">
                    <div class="card shadow dashboard-card text-center p-3">
                        <h5>💰 Unpaid Bills</h5>
                        <h2 class="text-danger">2</h2>
                        <small>This month</small>
                    </div>
                </div>

                <div class="col-md-4 mb-3">
                    <div class="card shadow dashboard-card text-center p-3">
                        <h5>🔔 Notifications</h5>
                        <h2 class="text-warning">1</h2>
                        <small>New messages</small>
                    </div>
                </div>

                <div class="col-md-4 mb-3">
                    <div class="card shadow dashboard-card text-center p-3">
                        <h5>🔧 Service Requests</h5>
                        <h2 class="text-success">0</h2>
                        <small>Pending</small>
                    </div>
                </div>

            </div>

            <!-- FEATURE SECTION -->
            <h4 class="mt-5 mb-3">Features</h4>

            <div class="row">

                <div class="col-md-4 mb-3">
                    <div class="card feature-card shadow text-center p-4"
                         onclick="location.href = '${pageContext.request.contextPath}/room?action=categories'">
                        <h5>🏠 Room</h5>
                        <p>View your room details</p>
                    </div>
                </div>

                <div class="col-md-4 mb-3">
                    <div class="card feature-card shadow text-center p-4"
                         onclick="location.href = '${pageContext.request.contextPath}/views/customer/bills.jsp'">
                        <h5>💰 Bills</h5>
                        <p>View payment history</p>
                    </div>
                </div>

                <div class="col-md-4 mb-3">
                    <div class="card feature-card shadow text-center p-4"
                         onclick="location.href = '${pageContext.request.contextPath}/views/customer/services.jsp'">
                        <h5>🔧 Services</h5>
                        <p>Request maintenance</p>
                    </div>
                </div>

                <div class="col-md-4 mb-3">
                    <div class="card feature-card shadow text-center p-4"
                         onclick="location.href = '${pageContext.request.contextPath}/views/customer/notifications.jsp'">
                        <h5>🔔 Notifications</h5>
                        <p>Latest announcements</p>
                    </div>
                </div>

                <div class="col-md-4 mb-3">
                    <div class="card feature-card shadow text-center p-4"
                         onclick="location.href = '${pageContext.request.contextPath}/views/customer/profile.jsp'">
                        <h5>👤 Profile</h5>
                        <p>Manage account</p>
                    </div>
                </div>

            </div>

        </div>

    </body>
</html>