// fetch GET용
async function fetchGet(url){

    console.log("fetchData1...1");

    try{
        console.log("fetchData1...2");
        const response = await fetch(url);
        console.log("here1");

        if(!response.ok){
            console.log("here2");
            throw new Error('response not ok');
        }

        const data = await response.json();
        console.log("data1 : " + data);

        return data;

    }catch (err) {
        console.log(err)
    }
}

// fetch POST용
async function fetchPost(url, jsonData){

    console.log("fetchData2...1");

    try{
        console.log("fetchData2...2");
        const response = await fetch(url, {
            method: 'POST',
            headers: {"Content-type":"application/json"},
            body: JSON.stringify(jsonData)
        });
        console.log("fetchData2...3");

        if(!response.ok){
            console.log("fetchData2...4");
            throw new Error('response not ok');
        }

        const data = await response.json();
        console.log("fetchData2...5 : " + data);

        return data;

    }catch (err) {
        console.log(err)
    }
}

// fetch DELETE용
async function fetchDelete(url){

    try{
        const response = await fetch(url, {
            method: 'DELETE'
        });

        if(!response.ok){
            throw new Error('response not ok');
        }

        const data = await response.json();
        console.log("data1 : " + data);

        return data;

    }catch (err) {
        console.log(err)
    }
}


// fetch PUT용
async function fetchPut(url, jsonData){

    try{
        const response = await fetch(url, {
            method: 'PUT',
            headers: {"Content-type":"application/json"},
            body: JSON.stringify(jsonData)
        });

        if(!response.ok){
            throw new Error('response not ok');
        }

        const data = await response.json();
        console.log("data1 : " + data);

        return data;

    }catch (err) {
        console.log(err)
    }
}

//modal
function alertModal(message){
    const modal = document.getElementById('alertModal');
    modal.getElementsByClassName('modal-body')[0].innerText = message;
    const resultModal = new bootstrap.Modal(modal);
    resultModal.show();
}

function editModal(message){
    const modal = document.getElementById('editModal');
    modal.getElementsByClassName('modal-title')[0].innerText = message + '수정';
    modal.getElementsByClassName('modal-body-comment')[0].innerText = message ;
    const resultModal = new bootstrap.Modal(modal);
    resultModal.show();
}

function closeEditModal(){
    var modalElement = document.getElementById('editModal'); // 모달 요소 가져오기
    var modal = bootstrap.Modal.getInstance(modalElement); // 모달 객체 가져오기
    modal.hide();
}

function editMailModal(message){
    const modal = document.getElementById('editMailModal');
    modal.getElementsByClassName('modal-title')[0].innerText = message + '수정';
    modal.getElementsByClassName('modal-body-comment')[0].innerText = message ;
    const resultModal = new bootstrap.Modal(modal);
    resultModal.show();
}

function closeEditMailModal(){
    var modalElement = document.getElementById('editMailModal'); // 모달 요소 가져오기
    var modal = bootstrap.Modal.getInstance(modalElement); // 모달 객체 가져오기
    modal.hide();
}

function editAddrModal(){
    const modal = document.getElementById('editAddrModal');
    const resultModal = new bootstrap.Modal(modal);
    resultModal.show();
}

function closeEditZipModal(){
    var modalElement = document.getElementById('editAddrModal'); // 모달 요소 가져오기
    var modal = bootstrap.Modal.getInstance(modalElement); // 모달 객체 가져오기
    modal.hide();
}

async function confirmModal(message){

    const result = await promiseConfirmModal(message);
    console.log("result1 : " + result);
    return result;
}

function promiseConfirmModal(message){

    return new Promise(resolve => {

        const confirmModal = document.getElementById('confirmModal');
        const btnOk = document.getElementById('btnOk');
        const btnCancel = document.getElementById('btnCancel');


        const modal = new bootstrap.Modal(confirmModal);

        // 확인 버튼 클릭 시
        btnOk.addEventListener('click', function (){
            resolve(true);
        });

        // 취소 버튼 클릭 시
        btnCancel.addEventListener('click', function (){
            resolve(false);
        });

        modal.show(); // 모달 열기
    });



}

function closeconfirmModal(){
    var modalElement = document.getElementById('confirmModal'); // 모달 요소 가져오기
    var modal = bootstrap.Modal.getInstance(modalElement); // 모달 객체 가져오기
    modal.hide();
}

function showInputValid(inputs){
    for(const input of inputs){
        input.classList.remove('is-invalid');
        input.classList.add('is-valid');
    }
}

function showInputInvalid(inputs){
    for(const input of inputs) {
        input.classList.remove('is-valid');
        input.classList.add('is-invalid');
    }
}

function showResultValid(result, message){
    result.classList.remove('invalid-feedback');
    result.classList.add('valid-feedback');
    result.innerText = message;
}

function showResultInvalid(result, message){
    result.classList.remove('valid-feedback');
    result.classList.add('invalid-feedback');
    result.innerText = message;
}

function hideResultInvalid() {
    // 결과 메시지 요소를 찾습니다.
    var resultElement = document.getElementById('resultComment');
    // 결과 메시지가 존재한다면
    if (resultElement) {
        // 메시지를 숨깁니다.
        resultElement.innerText = '';
    }
}



function validateNumber(input) {
    // 입력값에서 숫자 및 소수점을 제외한 문자 제거
    input.value = input.value.replace(/[^0-9.]/g, '');

    // 소수점이 두 번 이상 나타나지 않도록 제어
    if (input.value.split('.').length > 2) {
        input.value = input.value.replace(/\.+$/, '');
    }

    // 소수점으로 시작하거나 두 번째 소수점을 입력하는 경우 첫 번째 소수점만 유지
    if (input.value.startsWith('.')) {
        input.value = '0' + input.value;
    }
}