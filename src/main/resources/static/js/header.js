$(document).ready(function() {
    $('.select2-tag').select2({
        'ajax' : {
            'url': '/guide/tag',
            'data': function (param) {
                return {'tag' : param.term}
            },
            'processResults': function (data) {
                var _group;
                var _option;

                return data;
            }
        },
        'placeholder':"Search Tag"

    });
});

