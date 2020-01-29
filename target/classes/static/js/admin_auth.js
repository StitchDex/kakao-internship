var selectAccount = $('select.select2-account');
if (selectAccount.length > 0) {
    selectAccount.select2({
        'ajax' : {
            'url': '/admin/suggest',
            'data': function(params) {
                return {'userid' : params.term}
            },
            'processResults': function (data) {
                var ret = $.map(data, function (obj) {
                    obj.id = obj.id || obj.user_id; // replace pk with your identifier
                    obj.text = obj.text || obj.user_id; // replace pk with your identifier
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
                options += "<option value='" + val.user_key + "'>" + val.user_id +"</option>"
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
        var temp = {'admin':val.text};
        admin_list.push(temp);
    })

    $.ajax({
        'url':'./insertAdmin',
        'headers': {"X-CSRF-TOKEN": token},
        'type':'POST',
        'contentType':'application/json',
        'dataType':'json',
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
        var temp = {"admin":val.text};
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
