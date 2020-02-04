var selectAccount = $('select.select2-account');

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

$(window).on('load', function () {
    var cur_path = window.location.pathname;
    if(cur_path == "/admin/admin_auth") {
        onload();
    }
    else{

    }
});

function onload() {
    $('select.select-account-all').empty();
    $.getJSON("/admin/getadminall", function(data){
        $.each(data, function(key, val){
                var options = $("<option></option>")
                    .attr('EmpNo',val.adminEmpNo)
                    .attr('AccountId',val.adminAccountId)
                    .attr('Name',val.adminName)
                    .text(val.adminAccountId + "(" + val.adminName + ")");
                $('select.select-account-all').append(options);
            });
        }
    );
}

function deleteAdd(param){
    $(param).parent("li").remove();
}

$('.insert-admin').click(function(e) {
    e.preventDefault();

    if($("#ccList").children("li").length > 10) {
        alert("참조는 최대 10명까지만 추가가 가능합니다.");
        return;
    }

    var selectAccount = $('select.select2-account').select2('data');
    if(selectAccount.length > 0) {
        var data = { 'memberName': selectAccount[0].identityDisplayName,
            'memberAccountId': selectAccount[0].accountId,
            'memberEmpNo': selectAccount[0].employeeNo,
            'personName': selectAccount[0].personName};
        var deleteBtn = $('<button class="close deleteCC" onclick="deleteAdd(this)"><span aria-hidden="true">×</span></button>');

        // 중복 검사
        var liArr = $("select.select-account-all").children("option");
        for(i=0; i<liArr.length; i++) {
            if(data.memberAccountId == $(liArr[i]).attr('accountid')) {
                $('.select2-account').val(null).trigger('change');
                alert("이미 관리자 권한을 갖고있는 아이디 입니다.");
                return;
            }
        }

        var li = $('<li class="list-group-item"></li>')
            .attr('empNo', data.memberEmpNo)
            .attr('accountId', data.memberAccountId)
            .attr('empName',data.personName)
            .text(data.memberName)
            .append(deleteBtn);
        $("#addList").prepend(li);
        $('.select2-account').val(null).trigger('change');
    }
})

function insertClick() {

    var token = $("meta[name='_csrf']").attr("content");
    var admin_list = [];

    var selected = $('#addList').children('li');
    $.each(selected, function (key, val) {
        var temp = {'adminEmpNo':val.getAttribute('empno'), 'adminAccountId':val.getAttribute('accountid'), 'adminName':val.getAttribute('empname')};
        admin_list.push(temp);
    })

    if(admin_list.length == 0) return;

    $.ajax({
        'url':'./insertAdmin',
        'headers': {"X-CSRF-TOKEN": token},
        'type':'POST',
        'contentType':'application/json',
        'data': JSON.stringify(admin_list),
        'success':function(){
            onload();
            alert('성공적으로 추가되었습니다.');
            $('#addList').children('li').remove();
        },
        'error':function(){
            alert('에러 발생');
        }
    });
}

function deleteClick() {
    var admin_list = [];
    var selected = $("select.select-account-all").find("option:selected");
    $.each(selected, function (key, val) {
        var temp = {"adminEmpNo":val.getAttribute("empno"), "text" : val.text};
        admin_list.push(temp);
    })

    if(admin_list.length==0) return;

    var confirmText = "관리자 권한을 삭제 하시겠습니까?\n";

    for(var i = 0; i < admin_list.length; i++){
        confirmText += admin_list[i]['text'] + "\n";
    }
    var realDelete = confirm(confirmText)
    if(realDelete == false) return;

    var token = $("meta[name='_csrf']").attr("content");

    admin_list = JSON.stringify(admin_list);
    $.ajax({
        'url':'./deleteAdmin',
        'headers': {"X-CSRF-TOKEN": token},
        'async':false,
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

    onChange();
}

function onChange(){
    $('#select-count').text($('.select-account-all').children("option:selected").length)
}