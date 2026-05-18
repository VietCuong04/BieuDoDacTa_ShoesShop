<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <div>
        <h4 class="fw-bold mb-1">Dashboard</h4>
        <p class="text-muted mb-0 small">Tổng quan hệ thống ShoesShop</p>
      </div>
      <span class="badge bg-light text-dark border px-3 py-2">
        <i class="bi bi-calendar3 me-1"></i>{{ today }}
      </span>
    </div>

    <!-- Stats Cards -->
    <div class="row g-3 mb-4">
      <div class="col-xl-3 col-md-6" v-for="s in statCards" :key="s.label">
        <div class="card border-0 shadow-sm h-100">
          <div class="card-body d-flex align-items-center gap-3">
            <div class="stat-icon-box" :style="{ background: s.bg }">
              <i :class="s.icon" class="text-white"></i>
            </div>
            <div>
              <div class="fs-4 fw-bold text-dark">{{ s.value }}</div>
              <div class="text-muted small">{{ s.label }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Charts Row -->
    <div class="row g-3 mb-4">
      <div class="col-lg-5">
        <div class="card border-0 shadow-sm h-100">
          <div class="card-header bg-white border-bottom">
            <h6 class="fw-bold mb-0"><i class="bi bi-pie-chart me-2"></i>Thống kê đơn hàng</h6>
          </div>
          <div class="card-body d-flex align-items-center justify-content-center" style="min-height: 310px;">
            <div v-if="hasOrderData" style="width: 260px; height: 260px;">
              <canvas ref="chartCanvas"></canvas>
            </div>
            <div v-else class="text-center text-muted py-4">
              <i class="bi bi-pie-chart fs-1 d-block mb-2 opacity-25"></i>
              <span>Chưa có dữ liệu đơn hàng</span>
            </div>
          </div>
        </div>
      </div>
      <div class="col-lg-7">
        <div class="card border-0 shadow-sm h-100">
          <div class="card-header bg-white border-bottom">
            <h6 class="fw-bold mb-0"><i class="bi bi-bar-chart-line me-2"></i>Tổng quan đơn hàng</h6>
          </div>
          <div class="card-body d-flex align-items-center justify-content-center" style="min-height: 310px;">
            <div v-if="hasOrderData" style="width: 100%; height: 260px;">
              <canvas ref="barCanvas"></canvas>
            </div>
            <div v-else class="text-center text-muted py-4">
              <i class="bi bi-bar-chart fs-1 d-block mb-2 opacity-25"></i>
              <span>Chưa có dữ liệu đơn hàng</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Chat Statistics Section -->
    <div class="row g-3 mb-4">
      <div class="col-lg-6">
        <div class="card border-0 shadow-sm">
          <div class="card-header bg-white border-bottom">
            <h6 class="fw-bold mb-0"><i class="bi bi-chat-dots me-2"></i>Thống kê Chat</h6>
          </div>
          <div class="card-body">
            <div class="row g-3">
              <div class="col-6">
                <div class="text-center">
                  <div class="fs-4 fw-bold text-primary">{{ chatStats.successRate.toFixed(1) }}%</div>
                  <small class="text-muted">Tỷ lệ thành công</small>
                </div>
              </div>
              <div class="col-6">
                <div class="text-center">
                  <div class="fs-4 fw-bold text-success">{{ chatStats.uniqueUsers }}</div>
                  <small class="text-muted">Người dùng duy nhất</small>
                </div>
              </div>
              <div class="col-6">
                <div class="text-center">
                  <div class="fs-4 fw-bold text-warning">{{ chatStats.failedChats }}</div>
                  <small class="text-muted">Chat thất bại</small>
                </div>
              </div>
              <div class="col-6">
                <div class="text-center">
                  <div class="fs-4 fw-bold text-info">{{ chatStats.successfulChats }}</div>
                  <small class="text-muted">Chat thành công</small>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="col-lg-6">
        <div class="card border-0 shadow-sm">
          <div class="card-header bg-white border-bottom">
            <h6 class="fw-bold mb-0"><i class="bi bi-person-circle me-2"></i>Top người dùng Chat</h6>
          </div>
          <div class="card-body p-0">
            <div v-if="chatStats.topUsers && chatStats.topUsers.length > 0" class="list-group list-group-flush">
              <div v-for="(user, i) in chatStats.topUsers" :key="i" class="list-group-item d-flex justify-content-between align-items-center px-3 py-2">
                <div>
                  <small class="text-muted">{{ user.email }}</small>
                </div>
                <span class="badge bg-primary">{{ user.count }} lượt</span>
              </div>
            </div>
            <div v-else class="text-center text-muted py-3">
              <small>Chưa có dữ liệu</small>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Sample Questions Section -->
    <div class="row g-3 mb-4">
      <div class="col-lg-12">
        <div class="card border-0 shadow-sm">
          <div class="card-header bg-white border-bottom">
            <h6 class="fw-bold mb-0"><i class="bi bi-question-circle me-2"></i>Mẫu câu hỏi của khách</h6>
          </div>
          <div class="card-body">
            <div v-if="chatStats.sampleQuestions && chatStats.sampleQuestions.length > 0" class="row g-2">
              <div v-for="(q, i) in chatStats.sampleQuestions" :key="i" class="col-lg-6">
                <div class="p-3 bg-light rounded border-start border-primary border-3">
                  <small class="text-muted d-block mb-1">Q{{ i + 1 }}</small>
                  <p class="mb-0 text-dark" style="font-size: 0.95rem;">{{ truncateText(q, 80) }}</p>
                </div>
              </div>
            </div>
            <div v-else class="text-center text-muted py-3">
              <small>Chưa có câu hỏi</small>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Recent Orders & Quick Links -->
    <div class="row g-3">
      <div class="col-lg-8">
        <div class="card border-0 shadow-sm">
          <div class="card-header bg-white border-bottom d-flex justify-content-between align-items-center">
            <h6 class="fw-bold mb-0"><i class="bi bi-receipt me-2"></i>Đơn hàng gần đây</h6>
            <router-link to="/orders" class="btn btn-sm btn-outline-secondary">Xem tất cả</router-link>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table table-hover mb-0 align-middle">
                <thead class="table-light">
                  <tr>
                    <th class="ps-3">Mã</th>
                    <th>Khách hàng</th>
                    <th>Thanh toán</th>
                    <th>Trạng thái</th>
                    <th>Ngày tạo</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-if="recentOrders.length === 0">
                    <td colspan="5" class="text-center text-muted py-4">Chưa có đơn hàng</td>
                  </tr>
                  <tr v-for="o in recentOrders" :key="o.maDonHang">
                    <td class="ps-3 fw-semibold">#{{ o.maDonHang }}</td>
                    <td>{{ o.tenKhachHang || '—' }}</td>
                    <td class="fw-semibold">{{ formatPrice(o.soTienThanhToan) }}</td>
                    <td><span :class="statusClass(o.trangThai)">{{ statusLabelMap[o.trangThai] || o.trangThai || 'N/A' }}</span></td>
                    <td class="text-muted small">{{ formatDate(o.ngayKhoiTao) }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
      <div class="col-lg-4">
        <div class="card border-0 shadow-sm h-100">
          <div class="card-header bg-white border-bottom">
            <h6 class="fw-bold mb-0"><i class="bi bi-lightning me-2"></i>Truy cập nhanh</h6>
          </div>
          <div class="card-body">
            <div class="d-grid gap-2">
              <router-link to="/products" class="btn btn-outline-primary text-start">
                <i class="bi bi-box-seam me-2"></i>Quản lý sản phẩm
              </router-link>
              <router-link to="/orders" class="btn btn-outline-success text-start">
                <i class="bi bi-receipt me-2"></i>Quản lý đơn hàng
              </router-link>
              <router-link to="/discounts" class="btn btn-outline-warning text-start">
                <i class="bi bi-ticket-perforated me-2"></i>Quản lý giảm giá
              </router-link>
              <router-link to="/employees" class="btn btn-outline-info text-start">
                <i class="bi bi-people me-2"></i>Quản lý nhân viên
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { Chart, DoughnutController, BarController, ArcElement, BarElement, CategoryScale, LinearScale, Tooltip, Legend } from 'chart.js'

Chart.register(DoughnutController, BarController, ArcElement, BarElement, CategoryScale, LinearScale, Tooltip, Legend)

const API = 'http://localhost:8080/api'

const stats = ref({ products: 0, brands: 0, colors: 0, sizes: 0, materials: 0, origins: 0, orders: 0, employees: 0, discounts: 0 })
const chatStats = ref({ totalChats: 0, chatsToday: 0, successfulChats: 0, successRate: 0, failedChats: 0, uniqueUsers: 0, topUsers: [], sampleQuestions: [] })
const orderStats = ref({})
const recentOrders = ref([])
const chartCanvas = ref(null)
const barCanvas = ref(null)
const today = new Date().toLocaleDateString('vi-VN', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })

const statCards = computed(() => [
  { label: 'Sản phẩm', value: stats.value.products, icon: 'bi bi-box-seam fs-5', bg: 'linear-gradient(135deg, #6366f1, #818cf8)' },
  { label: 'Đơn hàng', value: stats.value.orders, icon: 'bi bi-receipt fs-5', bg: 'linear-gradient(135deg, #10b981, #34d399)' },
  { label: 'Giảm giá', value: stats.value.discounts, icon: 'bi bi-ticket-perforated fs-5', bg: 'linear-gradient(135deg, #f59e0b, #fbbf24)' },
  { label: 'Nhân viên', value: stats.value.employees, icon: 'bi bi-people fs-5', bg: 'linear-gradient(135deg, #ef4444, #f87171)' },
])

const hasOrderData = computed(() => Object.values(orderStats.value).some(v => v > 0))

const statusLabelMap = {
  'CHO_XAC_NHAN': 'Chờ xác nhận',
  'DA_XAC_NHAN': 'Đã xác nhận',
  'DANG_GIAO': 'Đang giao',
  'HOAN_THANH': 'Hoàn thành',
  'DA_HUY': 'Đã hủy',
}

const statusColorMap = {
  'CHO_XAC_NHAN': '#fbbf24',
  'DA_XAC_NHAN': '#38bdf8',
  'DANG_GIAO': '#6366f1',
  'HOAN_THANH': '#22c55e',
  'DA_HUY': '#ef4444',
}

function formatPrice(v) {
  if (v == null) return '—'
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(v)
}

function formatDate(d) {
  return d ? new Date(d).toLocaleDateString('vi-VN') : ''
}

function truncateText(text, length) {
  if (!text) return ''
  return text.length > length ? text.substring(0, length) + '...' : text
}

function statusClass(status) {
  const map = {
    'CHO_XAC_NHAN': 'badge bg-warning text-dark',
    'DA_XAC_NHAN': 'badge bg-info',
    'DANG_GIAO': 'badge bg-primary',
    'HOAN_THANH': 'badge bg-success',
    'DA_HUY': 'badge bg-danger',
  }
  return map[status] || 'badge bg-secondary'
}

function renderCharts() {
  const keys = Object.keys(statusLabelMap)
  const labels = keys.map(k => statusLabelMap[k])
  const data = keys.map(k => orderStats.value[k] || 0)
  const colors = keys.map(k => statusColorMap[k])

  if (chartCanvas.value) {
    new Chart(chartCanvas.value, {
      type: 'doughnut',
      data: {
        labels,
        datasets: [{
          data,
          backgroundColor: colors,
          borderWidth: 2,
          borderColor: '#fff',
          hoverOffset: 6,
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        cutout: '60%',
        plugins: {
          legend: {
            position: 'bottom',
            labels: { padding: 12, usePointStyle: true, pointStyleWidth: 10, font: { size: 11 } }
          },
          tooltip: {
            callbacks: { label: (ctx) => ` ${ctx.label}: ${ctx.raw} đơn` }
          }
        }
      }
    })
  }

  if (barCanvas.value) {
    new Chart(barCanvas.value, {
      type: 'bar',
      data: {
        labels,
        datasets: [{
          label: 'Số đơn hàng',
          data,
          backgroundColor: colors.map(c => c + 'cc'),
          borderColor: colors,
          borderWidth: 1,
          borderRadius: 6,
          barThickness: 40,
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
          tooltip: { callbacks: { label: (ctx) => ` ${ctx.raw} đơn hàng` } }
        },
        scales: {
          y: { beginAtZero: true, ticks: { stepSize: 1, font: { size: 11 } }, grid: { color: '#f0f0f0' } },
          x: { ticks: { font: { size: 11 } }, grid: { display: false } }
        }
      }
    })
  }
}

onMounted(async () => {
  const user = JSON.parse(localStorage.getItem('admin_user') || 'null')
  try {
    const headers = user ? { 'X-User-Id': String(user.maTaiKhoan), 'Authorization': `Bearer ${localStorage.getItem('admin_token')}` } : {}
    const [p, b, c, s, m, o, ord, emp, disc, ordStats, chatData] = await Promise.all([
      fetch(`${API}/products`).then(r => r.json()),
      fetch(`${API}/brands`).then(r => r.json()),
      fetch(`${API}/colors`).then(r => r.json()),
      fetch(`${API}/sizes`).then(r => r.json()),
      fetch(`${API}/materials`).then(r => r.json()),
      fetch(`${API}/origins`).then(r => r.json()),
      fetch(`${API}/orders`, { headers }).then(r => r.ok ? r.json() : []).catch(() => []),
      fetch(`${API}/employees`, { headers }).then(r => r.ok ? r.json() : []).catch(() => []),
      fetch(`${API}/discounts`).then(r => r.ok ? r.json() : []).catch(() => []),
      fetch(`${API}/orders/stats`).then(r => r.ok ? r.json() : {}).catch(() => ({})),
      fetch(`${API}/chat/stats`, { headers }).then(r => r.ok ? r.json() : { totalChats: 0, chatsToday: 0, successfulChats: 0, successRate: 0, failedChats: 0, uniqueUsers: 0, topUsers: [], sampleQuestions: [] }).catch(() => ({ totalChats: 0, chatsToday: 0, successfulChats: 0, successRate: 0, failedChats: 0, uniqueUsers: 0, topUsers: [], sampleQuestions: [] })),
    ])
    stats.value = {
      products: p.length, brands: b.length, colors: c.length,
      sizes: s.length, materials: m.length, origins: o.length,
      orders: ord.length, employees: emp.length, discounts: disc.length
    }
    chatStats.value = chatData
    orderStats.value = ordStats
    recentOrders.value = ord.slice(0, 5)

    await nextTick()
    if (hasOrderData.value) {
      renderCharts()
    }
  } catch (e) { console.error(e) }
})
</script>

<style scoped>
.stat-icon-box {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
</style>
