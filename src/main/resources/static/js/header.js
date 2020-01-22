$(document).ready(function() {
    $('.select2-tag').select2({
        'ajax' : {
            'url': '/guide/tag',
            'data': function (param) {
                return {'tag' : param.term}
            },
            'processResults': function (data) {
<<<<<<< HEAD
                Group(data); // grouping option
=======
                var _group;
                var _option;
>>>>>>> 3e8f2b2da293964703ca20a2dd6d7c04e4cdd80f

                return data;
            }
        },
        'placeholder':"Search Tag"

    });
});

<<<<<<< HEAD
//tag select event
$('.select2-tag').on('select2:select', function(e){
    $.ajax
    ({
        'url':"/guide/menu",
        'data': {'doc_key' : e.params.data.doc_key},
        'success': function(){
            console.log("tag request succ");
        },
        'error' : function () {
            console.log("tag request fail");
        }
    })
});

function Group(data) {
    var optGroup = [{}];
    var opts = [];

    for(var i = 0; i < data.length; i ++)
    {

    }
}
=======
>>>>>>> 3e8f2b2da293964703ca20a2dd6d7c04e4cdd80f
