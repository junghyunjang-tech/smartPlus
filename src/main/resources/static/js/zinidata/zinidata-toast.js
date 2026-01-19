/**
 * Zinidata Toast 알림 모듈
 * 
 * 기능:
 * - Toast 알림 표시 (success, error, info)
 * - 자동 삭제 (3초)
 * - 다중 Toast 지원
 * 
 * 의존성: jQuery
 * 
 * @author NICE ZiniData 개발팀
 * @since 1.0
 */

$(document).ready(function() {
    // Zinidata 네임스페이스 초기화
    if (typeof Zinidata === 'undefined') {
        window.Zinidata = {
            modules: {}
        };
    }
    
    // Toast 모듈 등록
    Zinidata.modules.toast = true;
    
    /**
     * Toast 알림 표시
     */
    Zinidata.toast = {
        /**
         * 성공 메시지 표시
         * @param {string} message 표시할 메시지
         */
        success: function(message) {
            this._show(message, 'success');
        },
        
        /**
         * 에러 메시지 표시
         * @param {string} message 표시할 메시지
         */
        error: function(message) {
            this._show(message, 'error');
        },
        
        /**
         * 정보 메시지 표시
         * @param {string} message 표시할 메시지
         */
        info: function(message) {
            this._show(message, 'info');
        },
        
        /**
         * Toast 표시 (내부 메서드)
         * @param {string} message 메시지
         * @param {string} type 타입 (success, error, info)
         * @private
         */
        _show: function(message, type) {
            // Toast 컨테이너 확인/생성
            let container = $('#toast-container');
            if (container.length === 0) {
                container = $('<div id="toast-container" class="toast-container"></div>');
                $('body').append(container);
            }
            
            // Toast 요소 생성
            const toast = $('<div class="toast toast-' + type + '">' + message + '</div>');
            container.append(toast);
            
            // 애니메이션: 나타나기
            setTimeout(function() {
                toast.addClass('show');
            }, 10);
            
            // 3초 후 자동 삭제
            setTimeout(function() {
                toast.removeClass('show');
                setTimeout(function() {
                    toast.remove();
                }, 300); // fade-out 애니메이션 시간
            }, 3000);
        }
    };
    
    console.log('Zinidata Toast 모듈 초기화 완료');
});
