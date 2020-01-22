var selectAccount = $('select.select2-account');
if (selectAccount.length > 0) {
    selectAccount.select2({
        ajax : {
            url: '/admin/suggest',
            data: function(params) {
                return {'userid' : params.term}
            },
            processResults: function (data) {
                var ret = $.map(data, function (obj) {
                    obj.id = obj.id || obj.user_key; // replace pk with your identifier
                    obj.text = obj.text || obj.user_id; // replace pk with your identifier
                    return obj;
                });
                return {results : ret}
            }
        }, //ajax end
        placeholder: '아이디를 검색하고 추가 버튼을 눌러주세요. (최대 10명까지)',
        minimumInputLength: 2,
        language: 'ko',
        allowClear: true
    });
}