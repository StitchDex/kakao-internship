var selectAccount = $('select.select2-account');
if (selectAccount.length > 0) {
    selectAccount.select2({
        'ajax' : {
            'url': '/admin/suggest',
            'data': function(params) {
                return {'accountId' : params.term}
            },
            'processResults': function (data) {
                var ret = $.map(data, function (obj) {
                    obj.id = obj.id || obj.accountId;
                    obj.text = obj.text || obj.identityDisplayName;
                    return obj;
                });
                return {results : ret}
            }
        }, //ajax end
        'placeholder': '아이디를 검색하고 추가 버튼을 눌러주세요. (최대 10명까지)',
        'minimumInputLength': 2,
        'language': 'ko',
        'allowClear': true,
    });
}

function onload() {
    var options = "";
    $.getJSON("/admin/getadminall", function(data){
            $.each(data, function(key, val){
                options += "<option value='" + val.adminEmpNo + "'>" + val.adminAccountId +
                   "(" + val.adminName + ")" + "</option>"
            });
            $('select.select-account-all').html(options);
        }
    );
}

function insert_click() {
    var token = $("meta[name='_csrf']").attr("content");
    var admin_list = [];

    var selected = $('select.select2-account').select2('data');
    $.each(selected, function (key, val) {
        var temp = {'adminEmpNo':val.employeeNo, 'adminAccountId':val.accountId, 'adminName':val.personName};
        admin_list.push(temp);
    })

    $.ajax({
        'url':'./insertAdmin',
        'headers': {"X-CSRF-TOKEN": token},
        'type':'POST',
        'contentType':'application/json',
        'data': JSON.stringify(admin_list),
        'success':function(){
            onload();
            alert('성공적으로 추가되었습니다.');
        },
        'error':function(){
            alert('에러 발생')
        }
    });
}

function delete_click() {
    var token = $("meta[name='_csrf']").attr("content");
    var admin_list = [];

    var selected = $("select.select-account-all option:selected");
    $.each(selected, function (key, val) {
        var temp = {"adminEmpNo":val.value, "adminAccountId":val.text};
        admin_list.push(temp);
    })

    admin_list = JSON.stringify(admin_list);
    $.ajax({
        'url':'./deleteAdmin',
        'headers': {"X-CSRF-TOKEN": token},
        'type':'POST',
        'contentType':'application/json',
        'data': admin_list,
        'success':function(){
            onload();
            alert('성공적으로 삭제되었습니다.');
        },
        'error':function(){
            alert('에러 발생')
        }
    });
}
