/**
 * 회원가입 페이지 스크립트
 */
document.addEventListener("DOMContentLoaded", function () {
  // Elements
  const btnCheckId = document.getElementById("btnCheckId");
  const memberIdInput = document.getElementById("memberId");
  const idCheckResult = document.getElementById("idCheckResult");

  let isIdChecked = false;

  // Event Listeners
  if (btnCheckId) {
    btnCheckId.addEventListener("click", checkIdDuplicate);
  }

  if (memberIdInput) {
    memberIdInput.addEventListener("input", function () {
      // Reset check status when input changes
      isIdChecked = false;
      idCheckResult.textContent = "";
      idCheckResult.className = "helper-text";
    });
  }

  // Functions
  function checkIdDuplicate() {
    const memberId = memberIdInput.value;

    if (!memberId) {
      if (typeof Zinidata !== 'undefined' && Zinidata.toast) {
        Zinidata.toast.error("아이디를 입력해주세요.");
      }
      memberIdInput.focus();
      return;
    }

    // Call API
    fetch(`/api/auth/check-id?memberId=${memberId}`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        if (data.success) {
          // data.data is true if duplicate exists, false if available
          const isDuplicate = data.data;

          if (isDuplicate) {
            idCheckResult.textContent = "이미 사용 중인 아이디입니다.";
            idCheckResult.className = "helper-text error";
            isIdChecked = false;
          } else {
            idCheckResult.textContent = "사용 가능한 아이디입니다.";
            idCheckResult.className = "helper-text success";
            isIdChecked = true;

            // Optional: Disable input to prevent changes after verification?
            // Usually better to let user change and re-verify.
          }
        } else {
          if (typeof Zinidata !== 'undefined' && Zinidata.toast) {
            Zinidata.toast.error(data.message || "오류가 발생했습니다.");
          }
        }
      })
      .catch((error) => {
        console.error("Error:", error);
        if (typeof Zinidata !== 'undefined' && Zinidata.toast) {
          Zinidata.toast.error("중복 확인 중 오류가 발생했습니다.");
        }
      });
  }

  // Form submission handler
  const signupForm = document.getElementById("signupForm");
  if (signupForm) {
    signupForm.addEventListener("submit", function(e) {
      e.preventDefault(); // Prevent default form submission
      
      // Get form data
      const formData = new FormData(signupForm);
      
      // Submit via AJAX
      fetch("/signup", {
        method: "POST",
        body: formData
      })
      .then(response => {
        // Check if it's a redirect (success case)
        if (response.redirected || response.ok) {
          // Show success toast
          if (typeof Zinidata !== 'undefined' && Zinidata.toast) {
            Zinidata.toast.success("회원가입이 완료되었습니다.");
          }
          // Redirect to login after 2 seconds
          setTimeout(function() {
            window.location.href = "/login";
          }, 2000);
        } else {
          // If not OK, try to read text response
          return response.text();
        }
      })
      .then(html => {
        if (html) {
          // Parse error message from HTML if present
          // This is a fallback - ideally we'd have an API
          const parser = new DOMParser();
          const doc = parser.parseFromString(html, 'text/html');
          const errorDiv = doc.querySelector('.alert-error span');
          if (errorDiv && typeof Zinidata !== 'undefined' && Zinidata.toast) {
            Zinidata.toast.error(errorDiv.textContent);
          }
        }
      })
      .catch(error => {
        console.error("Error:", error);
        if (typeof Zinidata !== 'undefined' && Zinidata.toast) {
          Zinidata.toast.error("회원가입 중 오류가 발생했습니다.");
        }
      });
    });
  }

  // Prevent form submission if ID not checked?
  // User didn't strictly ask for this, but it's good practice.
  // For now, I'll stick to the requested "button and logic".
});
