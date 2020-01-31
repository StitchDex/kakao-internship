$(document).ready(function() {
    $('.select2-tag').select2({
        'ajax' : {
            'url': '/guide/tag',
            'data': function (param) {
                return {'tag' : param.term}
            },
            'processResults': function (data) {
                var ret = $.map(data, function(obj){
                    obj.id = obj.id || obj.tag;
                    obj.text = obj.text || obj.tag;
                    return obj;
                })
                return {'results' : ret};

            }
        },
        'placeholder':"#Tag Search",
        'minimumInputLength': 1
    });
});
//tag select event
    $('.select2-tag').on('select2:select', function (e) {
        var url = "/guide/search?tag=" + encodeURIComponent(e.params.data.tag);
        location.href = url;
        $('.select2-tag').val(null).trigger("change");

    });