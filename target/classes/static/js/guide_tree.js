//for ckeditor
var doc_editor;
//document ready
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

    ClassicEditor
        .create(document.querySelector('#Guide_Doc'),
        )
        .then(editor => {
            editor.set('ReadOnly',true);
            doc_editor=editor;
        })
        .catch(error => {
                console.error(error);
            }
        );

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
                    doc_editor.setData(res);
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

