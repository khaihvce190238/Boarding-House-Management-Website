<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>New Contract - AKDD House</title>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body { background-color:#f4f6f9; }
        .page-header { background:linear-gradient(135deg,#1a1a2e,#16213e,#0f3460); color:white; border-radius:12px; padding:18px 24px; margin-bottom:24px; }
        .form-card { border-radius:16px; border:none; }
        .section-title { font-size:12px; font-weight:700; text-transform:uppercase; letter-spacing:.5px; color:#6c757d; margin-bottom:12px; }
        .form-control:focus,.form-select:focus { border-color:#0f3460; box-shadow:0 0 0 .2rem rgba(15,52,96,.2); }
        .btn-save { background:linear-gradient(135deg,#1a1a2e,#0f3460); border:none; color:white; }
        .btn-save:hover { opacity:.9; color:white; }
        .room-card { border-radius:10px; cursor:pointer; transition:.2s; border:2px solid #dee2e6; }
        .room-card:hover,.room-card.selected { border-color:#0f3460; background:#f0f4ff; }
        .room-card.selected .room-check { display:inline-block !important; }
    </style>
</head>
<body>
<%@ include file="../../navbar.jsp" %>
<div class="container mt-4 mb-5" style="max-width:760px">

    <div class="page-header d-flex align-items-center gap-3">
        <div class="rounded-circle bg-white bg-opacity-25 d-flex align-items-center justify-content-center" style="width:52px;height:52px;font-size:22px">
            <i class="bi bi-file-earmark-plus-fill"></i>
        </div>
        <div>
            <h4 class="mb-0 fw-bold">Sign New Contract</h4>
            <small class="opacity-75">Create a rental agreement for a room</small>
        </div>
        <a href="${pageContext.request.contextPath}/contract?action=list" class="btn btn-light btn-sm ms-auto">
            <i class="bi bi-arrow-left me-1"></i>Back
        </a>
    </div>

    <form action="${pageContext.request.contextPath}/contract" method="post">
        <input type="hidden" name="action" value="insert">

        <%-- Select Room --%>
        <div class="card form-card shadow-sm mb-3">
            <div class="card-body p-4">
                <div class="section-title"><i class="bi bi-door-open me-1"></i>Select Available Room <span class="text-danger">*</span></div>
                <c:choose>
                    <c:when test="${empty availableRooms}">
                        <div class="alert alert-warning mb-0"><i class="bi bi-exclamation-triangle me-2"></i>No available rooms at the moment.</div>
                    </c:when>
                    <c:otherwise>
                        <div class="row g-2" id="roomGrid">
                            <c:forEach var="r" items="${availableRooms}">
                                <div class="col-6 col-md-4">
                                    <div class="room-card p-3" onclick="selectRoom(${r.roomId}, '${r.roomNumber}', ${r.basePrice})">
                                        <div class="d-flex justify-content-between align-items-start">
                                            <div>
                                                <div class="fw-bold">Room ${r.roomNumber}</div>
                                                <div class="text-muted small">${r.categoryName}</div>
                                                <div class="text-success small fw-semibold mt-1">${r.basePrice}đ/month</div>
                                            </div>
                                            <i class="bi bi-check-circle-fill text-primary room-check" style="display:none"></i>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        <input type="hidden" name="roomId" id="roomIdInput" required>
                        <div id="selectedRoomLabel" class="mt-2 text-muted small">No room selected</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <%-- Primary Tenant --%>
        <div class="card form-card shadow-sm mb-3">
            <div class="card-body p-4">
                <div class="section-title"><i class="bi bi-person me-1"></i>Primary Tenant <span class="text-danger">*</span></div>
                <select name="primaryUserId" class="form-select" required>
                    <option value="">— Select tenant —</option>
                    <c:forEach var="cu" items="${customers}">
                        <option value="${cu.userId}">${not empty cu.fullName ? cu.fullName : cu.username} (@${cu.username})</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <%-- Contract Terms --%>
        <div class="card form-card shadow-sm mb-3">
            <div class="card-body p-4">
                <div class="section-title"><i class="bi bi-calendar3 me-1"></i>Contract Terms</div>
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Start Date <span class="text-danger">*</span></label>
                        <input type="date" name="startDate" class="form-control" required value="${today}">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">End Date <span class="text-muted fw-normal">(optional)</span></label>
                        <input type="date" name="endDate" class="form-control">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Deposit Amount (đ) <span class="text-danger">*</span></label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-cash"></i></span>
                            <input type="number" name="deposit" id="depositInput" class="form-control" placeholder="0" min="0" required>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="d-flex gap-2">
            <button type="submit" class="btn btn-save flex-fill fw-semibold py-2">
                <i class="bi bi-file-earmark-check me-1"></i>Sign Contract
            </button>
            <a href="${pageContext.request.contextPath}/contract?action=list" class="btn btn-outline-secondary flex-fill fw-semibold py-2">
                <i class="bi bi-x-circle me-1"></i>Cancel
            </a>
        </div>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
let selectedRoomId = null;
function selectRoom(id, number, price) {
    selectedRoomId = id;
    document.getElementById('roomIdInput').value = id;
    document.getElementById('depositInput').value = price;
    document.getElementById('selectedRoomLabel').textContent = 'Selected: Room ' + number;
    document.getElementById('selectedRoomLabel').className = 'mt-2 text-primary small fw-semibold';
    document.querySelectorAll('.room-card').forEach(c => c.classList.remove('selected'));
    event.currentTarget.classList.add('selected');
}
// Set today as default start date
document.addEventListener('DOMContentLoaded', function() {
    var today = new Date().toISOString().substring(0,10);
    document.querySelector('[name=startDate]').value = today;
});
</script>
</body>
</html>
