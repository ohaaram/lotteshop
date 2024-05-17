// 판매자회원 유효성 검사 및 중복검사

//유효성 검사에 사용할 상태변수
let isUidOk = false;
let isPassOk = false;
let isNameOk = false;
let isCeoOk = false;
let isEmailOk = false;
let isHpOk = false;
let isEmailCodeOk = false;
let isBizRegNumOk = false;
let isFaxOk = false;
let isTelOk = false;
let isDNameOk = false;

// 유효성 검사에 사용할 정규표현식
const reUid = /^[a-z]+[a-z0-9]{4,19}$/g;
const rePass = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{5,16}$/;
const reName = /^(?:\((주)\))?\s*[가-힣]{2,10}$/;
const reCeo = /^[가-힣]{2,10}$/;
const reEmail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
const reBizRegNum = /^[0-9]{3}-[0-9]{2}-[0-9]{5}$/;
const reTel = /^(0[2-8][0-5]?)-?([1-9]{1}[0-9]{2,3})-?([0-9]{4})$/;
const reHp = /^01(?:0|1|[6-9])-(?:\d{4})-\d{4}$/;
const reFax = /^\d{2,3}-\d{3,4}-\d{4}$/;

window.onload = function () {

    // 아이디 유효성 검사
    const btnCheckUid = document.getElementById('btnCheckUid');
    const resultUid = document.getElementById('result_uid');

    btnCheckUid.onclick = function () {

        const type = this.dataset.type; //입력 유형 (uid, nickname 등)
        const input = document.registerForm[type]; // 입력 요소

        // 정규식 검사
        if (!input.value.match(reUid)) {

            console.log("여기에 들어오나?");
            input.classList.add('is-invalid');
            resultUid.style.color = 'red';
            resultUid.innerText = '아이디 형식이 맞지 않습니다.';
            isUidOk = false;
            return;
        }

        // 서버에 아이디 중복 확인 요청 (비동기 처리)
        async function fetchGet(url) {

            console.log("fetchData1...1");

            try {
                console.log("fetchData1...2");
                const response = await fetch(url);
                console.log("here1");

                if (!response.ok) {
                    console.log("here2");
                    throw new Error('response not ok');
                }

                const data = await response.json();
                console.log("data1 : " + data);
                return data;
            } catch (err) {
                console.log(err)
            }
        }

        setTimeout(async () => {

                console.log('value : ' + input.value);
                console.log('type:' + type);

                const data = await fetchGet(`/lotteshop/seller/check/${type}/${input.value}`);
                if (data.result > 0) { //중복 아이디 존재

                    input.classList.remove('is-valid'); // 기존의 유효한 클래스를 제거
                    input.classList.add('is-invalid');
                    resultUid.style.color = 'red';
                    resultUid.classList.add('invalid-feedback');
                    resultUid.innerText = '이미 사용중인 아이디 입니다.';
                    isUidOk = false;
                } else {// 사용 가능한 아이디

                    input.classList.remove('is-invalid'); // 기존의 무효한 클래스를 제거
                    input.classList.add('is-valid');

                    resultUid.classList.add('valid-feedback');
                    resultUid.style.color = 'green';
                    resultUid.innerText = '사용 가능한 아이디 입니다.';
                    isUidOk = true;
                    console.log("isUidOk:" + isUidOk);
                }
            }, 1000
        );
    }

    // 비밀번호 유효성 검사
    const resultPass = document.getElementById('result_pass');

    document.registerForm.sellerPass2.addEventListener('focusout', () => {

        const inputPass1 = document.registerForm.sellerPass;
        const inputPass2 = document.registerForm.sellerPass2;

        console.log("inputPass1 : " + inputPass1);
        console.log("inputPass2 : " + inputPass2);


        if (inputPass1.value === inputPass2.value) {

            if (!inputPass1.value.match(rePass)) {
                inputPass1.classList.add('is-invalid');
                inputPass2.classList.add('is-invalid');
                resultPass.innerText = '비밀번호 형식에 맞지 않습니다.';
                isPassOk = false;
            } else {
                inputPass1.classList.add('is-valid');
                inputPass2.classList.add('is-valid');
                resultPass.innerText = '사용 가능한 비밀번호 입니다.';
                isPassOk = true;
            }
        } else {
            inputPass1.classList.add('is-invalid');
            inputPass2.classList.add('is-invalid');
            resultPass.innerText = '비밀번호가 일치하지 않습니다.';
            isPassOk = false;
        }
    });

    // 회사명 유효성 검사
    const resultName = document.getElementById('result_name');

    document.registerForm.sellerName.addEventListener('focusout', () => {
        const type = document.registerForm.sellerName.dataset.type;
        const input = document.registerForm[type];

        // 정규식 검사
        if (!input.value.match(reName)) {
            input.classList.add('is-invalid');
            resultName.classList.add('invalid-feedback');
            resultName.innerText = '회사명 형식이 맞지 않습니다.';
            isNameOk = false;
            return;
        }

        async function fetchGet(url) {

            console.log("fetchData1...1");

            try {
                console.log("fetchData1...2");
                const response = await fetch(url);
                console.log("here1");

                if (!response.ok) {
                    console.log("here2");
                    throw new Error('response not ok');
                }

                const data = await response.json();
                console.log("data1 : " + data);
                return data;
            } catch (err) {
                console.log(err)
            }
        }

        setTimeout(async () => {

            const data = await fetchGet(`/lotteshop/seller/check/${type}/${input.value}`);

            if (data.result > 0) {
                input.classList.add('is-invalid');

                resultName.classList.add('invalid-feedback');
                resultName.innerText = '이미 사용중인 회사명 입니다.';
                isNameOk = false;
            } else {
                input.classList.add('is-valid');

                resultName.classList.add('valid-feedback');
                resultName.innerText = '사용 가능한 회사명 입니다.';
                isNameOk = true;

            }
        }, 1000);
    });
    // 대표자 유효성 검사
    const resultCEO = document.getElementById('result_ceo');

    document.registerForm.sellerCEO.addEventListener('focusout', () => {
        const value = document.registerForm.sellerCEO.value;
        const inputName = document.registerForm.sellerCEO;

        if (!value.match(reCeo)) {
            inputName.classList.add('is-invalid');
            resultCEO.classList.add('invalid-feedback');
            resultCEO.innerText = '이름 형식이 맞지 않습니다.';
            isNameOk = false;
        } else {
            resultCEO.innerText = '';
            isCeoOk = true;
        }
    });

    // 사업자 등록 유효성 검사
    const resultNick = document.getElementById('result_sellerSaNumber');

    document.registerForm.sellerSaNumber.addEventListener('focusout', () => {
        const type = document.registerForm.sellerSaNumber.dataset.type;
        const input = document.registerForm[type];


        // 정규식 검사
        if (!input.value.match(reBizRegNum)) {
            input.classList.add('is-invalid');
            resultNick.classList.add('invalid-feedback');
            resultNick.innerText = '사업자등록번호 형식이 맞지 않습니다.';
            isBizRegNumOk = false;
            return;
        }

        async function fetchGet(url) {

            console.log("fetchData1...1");

            try {
                console.log("fetchData1...2");
                const response = await fetch(url);
                console.log("here1");

                if (!response.ok) {
                    console.log("here2");
                    throw new Error('response not ok');
                }

                const data = await response.json();
                console.log("data1 : " + data);
                return data;
            } catch (err) {
                console.log(err)
            }
        }

        setTimeout(async () => {

            const data = await fetchGet(`/lotteshop/seller/check/${type}/${input.value}`);

            if (data.result > 0) {
                input.classList.add('is-invalid');

                resultNick.classList.add('invalid-feedback');
                resultNick.innerText = '이미 사용중인 사업자등록번호 입니다.';
                isBizRegNumOk = false;
            } else {
                input.classList.add('is-valid');

                resultNick.classList.add('valid-feedback');
                resultNick.innerText = '사용 가능한 사업자등록번호 입니다.';
                isBizRegNumOk = true;

            }
        }, 1000);
    });


    // 휴대폰 유효성 검사
    const resultHp = document.getElementById('result_hp');

    document.registerForm.sellerHp.addEventListener('focusout', () => {
        const type = document.registerForm.sellerHp.dataset.type;
        const input = document.registerForm[type];


        // 정규식 검사
        if (!input.value.match(reTel)) {
            input.classList.add('is-invalid');
            resultHp.classList.add('invalid-feedback');
            resultHp.innerText = '전화번호 형식이 맞지 않습니다.';
            isTelOk = false;
            return;
        }

        async function fetchGet(url) {

            console.log("fetchData1...1");

            try {
                console.log("fetchData1...2");
                const response = await fetch(url);
                console.log("here1");

                if (!response.ok) {
                    console.log("here2");
                    throw new Error('response not ok');
                }

                const data = await response.json();
                console.log("data1 : " + data);
                return data;
            } catch (err) {
                console.log(err)
            }
        }

        setTimeout(async () => {
            const data = await fetchGet(`/lotteshop/seller/check/${type}/${input.value}`);

            if (data.result > 0) {
                input.classList.add('is-invalid');

                resultHp.classList.add('invalid-feedback');
                resultHp.innerText = '이미 사용중인 전화번호 입니다.';
                isTelOk = false;
            } else {
                input.classList.add('is-valid');

                resultHp.classList.add('valid-feedback');
                resultHp.innerText = '사용 가능한 전화번호 입니다.';
                isTelOk = true;
            }
        }, 1000);
    });
    // 팩스 유효성 검사
    const resultFax = document.getElementById('result_fax');

    document.registerForm.sellerFax.addEventListener('focusout', () => {
        const type = document.registerForm.sellerFax.dataset.type;
        const input = document.registerForm[type];


        // 정규식 검사
        if (!input.value.match(reFax)) {
            input.classList.add('is-invalid');
            resultFax.classList.add('invalid-feedback');
            resultFax.innerText = '팩스 형식이 맞지 않습니다.';
            isFaxOk = false;
            return;
        }

        async function fetchGet(url) {

            console.log("fetchData1...1");

            try {
                console.log("fetchData1...2");
                const response = await fetch(url);
                console.log("here1");

                if (!response.ok) {
                    console.log("here2");
                    throw new Error('response not ok');
                }

                const data = await response.json();
                console.log("data1 : " + data);
                return data;
            } catch (err) {
                console.log(err)
            }
        }

        setTimeout(async () => {
            const data = await fetchGet(`/lotteshop/seller/check/${type}/${input.value}`);

            if (data.result > 0) {
                input.classList.add('is-invalid');

                resultFax.classList.add('invalid-feedback');
                resultFax.innerText = '이미 사용중인 팩스번호 입니다.';
                isFaxOk = false;
            } else {
                input.classList.add('is-valid');

                resultFax.classList.add('valid-feedback');
                resultFax.innerText = '사용 가능한 팩스번호 입니다.';
                isFaxOk = true;
            }
        }, 1000);
    });

    // 이메일 유효성 검사
    const divEmailCode = document.getElementById('divEmailCode');

    const inputEmail = document.getElementById('inputEmail');
    const btn_email = document.getElementById('btn_email');
    const resultEmail = document.getElementById('result_email');

    btn_email.onclick = function () {

        const type = this.dataset.type;

        console.log("type : " + type);

        // 유효성 검사
        if (!inputEmail.value.match(reEmail)) {

            inputEmail.classList.add('is-invalid');
            resultEmail.innerText = '이메일 형식이 맞지 않습니다.';
            isEmailOk = false;
            return;
        }

        async function fetchGet(url) {

            console.log("fetchData1...1");

            try {
                console.log("fetchData1...2");
                const response = await fetch(url);
                console.log("here1");

                if (!response.ok) {
                    console.log("here2");
                    throw new Error('response not ok');
                }

                const data = await response.json();
                console.log("data1 : " + data);
                return data;
            } catch (err) {
                console.log(err)
            }
        }

        // 이메일 인증코드 발급 및 중복체크 추가 예정
        setTimeout(async () => {

            console.log('inputEmail value : ' + inputEmail.value);

            const data = await fetchGet(`/lotteshop/seller/check/${type}/${inputEmail.value}`);
            console.log('data : ' + data.result);

            if (data.result > 0) {
                inputEmail.classList.add('is-invalid');
                resultEmail.innerText = '이미 사용중인 이메일 입니다.';
                isEmailOk = false;
            } else {
                alert('인증코드를 이메일로 전송합니다.');
            }

        }, 1000);

        isEmailOk = true;
    }
    // 이메일 인증코드 확인
    const btnCheckEmailCode = document.getElementById('btnCheckEmailCode');
    const inputEmailCode = document.getElementById('inputEmailCode');
    const resultEmailCode = document.getElementById('resultEmailCode');

    btnCheckEmailCode.onclick = async function () {

        async function fetchGet(url) {

            console.log("fetchData1...1");

            try {
                console.log("fetchData1...2");
                const response = await fetch(url);
                console.log("here1");

                if (!response.ok) {
                    console.log("here2");
                    throw new Error('response not ok');
                }

                const data = await response.json();
                console.log("data1 : " + data);
                return data;
            } catch (err) {
                console.log(err)
            }
        }

        const data = await fetchGet(`/lotteshop/member/email/${inputEmailCode.value}`);

        if (!data.result) {
            inputEmail.classList.add('is-invalid');
            inputEmailCode.classList.add('is-invalid');
            resultEmailCode.innerText = '인증코드가 일치하지 않습니다.';
            isEmailCodeOk = false;
        } else {
            inputEmail.classList.add('is-valid');
            inputEmailCode.classList.add('is-valid');
            resultEmailCode.innerText = '이메일이 인증되었습니다.';
            isEmailCodeOk = true;
        }
    }


    //우편번호 검색
    function postcode() {
        new daum.Postcode({
            oncomplete: function (data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('inputZip').value = data.zonecode;
                document.getElementById("inputAddr1").value = addr;
                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("inputAddr2").focus();
            }
        }).open();
    }

    findZip.onclick = function () {
        postcode();
    }

    // 담당자 이름 유효성 검사
    const resultSellerDName = document.getElementById('result_sellerDname');

    document.registerForm.sellerDname.addEventListener('focusout', () => {
        const value = document.registerForm.sellerDname.value;
        const inputName = document.registerForm.sellerDname;

        if (!value.match(reCeo)) {
            inputName.classList.add('is-invalid');
            resultSellerDName.classList.add('invalid-feedback');
            resultSellerDName.innerText = '이름 형식이 맞지 않습니다.';
            isDNameOk = false;
        } else {
            resultSellerDName.innerText = '';
            isDNameOk = true;
        }
    });

    //담당자 휴대폰번호 유효성 검사
    const resultSellerDHp = document.getElementById('result_sellerDHp');

    document.registerForm.sellerDHp.addEventListener('focusout', () => {
        const type = document.registerForm.sellerDHp.dataset.type;
        const input = document.registerForm[type];


        // 정규식 검사
        if (!input.value.match(reHp)) {
            input.classList.add('is-invalid');
            resultSellerDHp.classList.add('invalid-feedback');
            resultSellerDHp.innerText = ' 형식이 맞지 않습니다.';
            isHpOk = false;
            return;
        }

        async function fetchGet(url) {

            console.log("fetchData1...1");

            try {
                console.log("fetchData1...2");
                const response = await fetch(url);
                console.log("here1");

                if (!response.ok) {
                    console.log("here2");
                    throw new Error('response not ok');
                }

                const data = await response.json();
                console.log("data1 : " + data);
                return data;
            } catch (err) {
                console.log(err)
            }
        }

        setTimeout(async () => {
            const data = await fetchGet(`/lotteshop/seller/check/${type}/${input.value}`);

            if (data.result > 0) {
                input.classList.add('is-invalid');

                resultSellerDHp.classList.add('invalid-feedback');
                resultSellerDHp.innerText = '이미 사용중인 휴대전화 번호 입니다.';
                isHpOk = false;
            } else {
                input.classList.add('is-valid');

                resultSellerDHp.classList.add('valid-feedback');
                resultSellerDHp.innerText = '사용 가능한 휴대전화 번호 입니다.';
                isHpOk = true;
            }
        }, 1000);
    });

    // 최종 유효성 검사 확인
    document.registerForm.onsubmit = function () {

        if (!isUidOk) {
            alert("아이디가 유효하지 않습니다.!");
            return false;
        }

        if (!isPassOk) {
            alert('비밀번호가 유효하지 않습니다.');
            return false;
        }

        if (!isNameOk) {
            alert('회사명이 유효하지 않습니다.');
            return false;
        }

        if (!isCeoOk) {
            alert('대표자가 유효하지 않습니다.');
            return false;
        }

        if (!isEmailOk) {
            alert('이메일이 유효하지 않습니다.');
            return false;
        }

        if (!isEmailCodeOk) {
            alert('이메일 인증을 해주세요.');
            return false;
        }

        if (!isHpOk) {
            alert('휴대전화 번호가 유효하지 않습니다.');
            return false;
        }

        if (!isBizRegNumOk) {
            alert('사업자 번호가 유효하지 않습니다.');
            return false;
        }

        if (!isTelOk) {
            alert('전화번호가 유효하지 않습니다.');
            return false;
        }

        if (!isFaxOk) {
            alert('팩스번호가 유효하지 않습니다.');
            return false;
        }

        if (!isDNameOk) {
            alert('담당자 이름이 유효하지 않습니다.');
            return false;
        }

        if (document.getElementById('inputZip').value === '') {
            alert('주소를 입력해주세요');
            return false;
        }

        // 폼 전송
        return true;
    }
}

