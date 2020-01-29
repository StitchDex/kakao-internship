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
        'placeholder':"Search Tag",
        'minimumInputLength': 2
    });
});

//tag select event
$('.select2-tag').on('select2:select', function(e){
    $.ajax
    ({
        'url':"/guide/search",
        'data': {'tag' : e.params.data.tag},
        'success': function(res){
            console.log("tag request succ");
        },
        'error' : function () {
            console.log("tag request fail");
        }
    })
    $('.select2-tag').val(null).trigger("change");
});
