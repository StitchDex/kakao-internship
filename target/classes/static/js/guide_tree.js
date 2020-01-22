$(function () {
    $('#jstree').jstree({
        'core': {
            'multiple': false,
            "check_callback": true,
            'themes': {
                'responsive': true
            },
            'data':  {
                'url' : '/guide/tree',
                'type': "GET",
                'dataType' : 'json',

            }
        } ,
        'plugins' : ['wholerow']
    }).on('ready.jstree', function(){ $(this).jstree('open_all') });
});

$('#jstree').on('select_node.jstree', function (e, data) {
    var selectedData = data.node.id;
    if(selectedData.startsWith("DIR")){

    }
    else {
        selectedData = selectedData.substring(3,selectedData.length);
        if (!isNaN(selectedData)) { //숫자일때
            $.ajax({
                url: '/guide/menu?doc_Key=' + selectedData,
                method: 'GET',
                success: function (res) {
                    console.log(res);
                    //res = text 1)ckeditor(admin)  2)textarea(공통)
                }, error: function (error) {
                    console.log(error);
                    //에러처리
                }
            });
        } else { //숫자아닐때
            //에러처리
            console.log("not document");
        }
    }
});

/*
function pageSelect2() {
    if(window.jQuery && jQuery.isFunction($().select2)) {
        if ($('select.select2-ticket-id').length > 0) {
            $('select.select2-ticket-id').select2({
                'ajax': {
                    'url': '/suggest/ticket-id',
                    data: function (param) {
                        // Query
                        return {ticketCode: param.term}
                    }, processResults: function (data, param) {
                        // Results
                        data = $.map(data, function (obj) {
                            obj.id = obj.id || obj.ticketCode;
                            obj.text = obj.text || obj.ticketCode;
                            return obj;
                        });
                        return {results: data};
                    }
                }, //ajax end
                'placeholder': '티켓 아이디 태그를 선택해 주세요.(티켓 아이디 생성에 사용됩니다.)',
                'minimumInputLength': 2,
                'language': 'ko',
                'tags': true
            });
        }
    }
}*/
