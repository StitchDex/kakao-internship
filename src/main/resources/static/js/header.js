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
                var ret = $.map(data, function(obj){
                    obj.id = obj.id || obj.tag;
                    obj.text = obj.text || obj.tag;
                    return obj;
                })
                return {'results' : ret};
>>>>>>> bfb5e942cf78973fbd72e72e522aefcb133dac1d

            }
        },
        'placeholder':"Search Tag"

    });
});

<<<<<<< HEAD
=======

>>>>>>> bfb5e942cf78973fbd72e72e522aefcb133dac1d
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
<<<<<<< HEAD
}
=======
}
>>>>>>> bfb5e942cf78973fbd72e72e522aefcb133dac1d
