<template>
  <div class="reviews-section">
    <h3>Đánh giá sản phẩm</h3>
    
    <!-- Nếu có user đăng nhập, cho phép thêm đánh giá -->
    <div class="add-review-form" v-if="isLoggedIn">
      <div class="form-group">
        <label>Đánh giá (sao)</label>
        <div class="star-rating">
          <span 
            v-for="i in 5" 
            :key="i"
            @click="newReview.rating = i"
            :class="['star', { active: i <= newReview.rating }]"
          >★</span>
        </div>
      </div>
      
      <div class="form-group">
        <label>Bình luận</label>
        <textarea 
          v-model="newReview.comment" 
          placeholder="Chia sẻ cảm nhận của bạn về sản phẩm..."
          rows="4"
        ></textarea>
      </div>
      
      <button @click="submitReview" class="btn-submit" :disabled="submitting">
        {{ submitting ? 'Đang gửi...' : 'Gửi đánh giá' }}
      </button>
      <p v-if="error" class="error">{{ error }}</p>
    </div>

    <p v-if="!isLoggedIn" class="login-prompt">
      <router-link to="/login">Đăng nhập</router-link> để gửi đánh giá
    </p>

    <!-- Danh sách đánh giá -->
    <div class="reviews-list">
      <div v-if="reviews.length === 0" class="no-reviews">
        Chưa có đánh giá nào
      </div>
      
      <div v-for="review in reviews" :key="review.maDanhGia" class="review-item">
        <div class="review-header">
          <span class="reviewer-name">{{ review.tenNguoiDung }}</span>
          <span class="review-stars">
            <span v-for="i in 5" :key="i" :class="['star-icon', { filled: i <= review.soSao }]">★</span>
          </span>
          <span class="review-date">{{ formatDate(review.ngayTao) }}</span>
        </div>
        <div class="review-comment">{{ review.comment }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ReviewSection',
  props: {
    productId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      reviews: [],
      newReview: {
        rating: 5,
        comment: ''
      },
      submitting: false,
      error: '',
      isLoggedIn: false,
      currentUserId: null,
      API_BASE: 'https://shoes-web-be-t7xh.onrender.com/api'
    };
  },
  async mounted() {
    await this.loadReviews();
    this.checkLoginStatus();
  },
  methods: {
    async loadReviews() {
      try {
        const response = await fetch(`${this.API_BASE}/reviews/product/${this.productId}`);
        if (response.ok) {
          this.reviews = await response.json();
        }
      } catch (error) {
        console.error('Lỗi tải đánh giá:', error);
      }
    },
    checkLoginStatus() {
      const user = localStorage.getItem('user');
      if (user) {
        const userData = JSON.parse(user);
        this.isLoggedIn = true;
        this.currentUserId = userData.maTaiKhoan;
      }
    },
    async submitReview() {
      if (!this.newReview.comment.trim()) {
        this.error = 'Vui lòng nhập bình luận';
        return;
      }

      if (this.newReview.comment.length < 10) {
        this.error = 'Bình luận phải có ít nhất 10 ký tự';
        return;
      }

      this.submitting = true;
      this.error = '';

      try {
        const response = await fetch(`${this.API_BASE}/reviews`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          body: JSON.stringify({
            maSanPham: this.productId,
            maTaiKhoan: this.currentUserId,
            soSao: this.newReview.rating,
            comment: this.newReview.comment
          })
        });

        if (response.ok) {
          this.newReview = { rating: 5, comment: '' };
          await this.loadReviews();
          this.$emit('review-added');
        } else {
          this.error = 'Lỗi khi gửi đánh giá';
        }
      } catch (error) {
        console.error('Lỗi:', error);
        this.error = 'Lỗi khi gửi đánh giá';
      } finally {
        this.submitting = false;
      }
    },
    formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleDateString('vi-VN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      });
    }
  }
};
</script>

<style scoped>
.reviews-section {
  margin-top: 40px;
  padding: 20px;
  border-top: 2px solid #f0f0f0;
}

.reviews-section h3 {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.add-review-form {
  background: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
}

.star-rating {
  display: flex;
  gap: 5px;
  font-size: 30px;
}

.star {
  cursor: pointer;
  color: #ddd;
  transition: color 0.2s;
}

.star:hover,
.star.active {
  color: #ffc107;
}

textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-family: inherit;
  resize: vertical;
}

.btn-submit {
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}

.btn-submit:hover:not(:disabled) {
  background-color: #0056b3;
}

.btn-submit:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.error {
  color: #dc3545;
  margin-top: 10px;
  font-size: 14px;
}

.login-prompt {
  background: #e7f3ff;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
  color: #004085;
}

.login-prompt a {
  color: #0062cc;
  text-decoration: none;
  font-weight: 500;
}

.login-prompt a:hover {
  text-decoration: underline;
}

.reviews-list {
  margin-top: 30px;
}

.no-reviews {
  text-align: center;
  color: #999;
  padding: 20px;
}

.review-item {
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 15px;
}

.review-header {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 10px;
  flex-wrap: wrap;
}

.reviewer-name {
  font-weight: 600;
  color: #333;
}

.review-stars {
  display: flex;
  gap: 2px;
}

.star-icon {
  color: #ddd;
  font-size: 14px;
}

.star-icon.filled {
  color: #ffc107;
}

.review-date {
  color: #999;
  font-size: 12px;
  margin-left: auto;
}

.review-comment {
  color: #555;
  line-height: 1.5;
  white-space: pre-wrap;
  word-break: break-word;
}

@media (max-width: 768px) {
  .reviews-section {
    padding: 15px;
  }

  .add-review-form {
    padding: 15px;
  }

  .review-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .review-date {
    margin-left: 0;
  }

  .star-rating {
    font-size: 24px;
  }
}
</style>
