//for ckeditor
var doc_editor;

//document ready
$(function () {
    $('#jstree').jstree({
        'core': {
            'multiple': false,
            "check_callback": true,
            'themes': {
                'name' : 'default',
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

//click tree_node
$('#jstree').on('select_node.jstree', function (e, data) {
    let selectedData = data.node.id;
    //click dir_node
    if(selectedData.startsWith("DIR")){

    }
    //click page_node
    else {
        selectedData = selectedData.substring(3,selectedData.length);
        if (!isNaN(selectedData)) {
            $.ajax({
                url: '/guide/menu?doc_key=' + selectedData,
                method: 'GET',
                success: function (res) {
                    //set DOCUMENT_TEXT in editor area
                    make_editor(res);
                }, error: function (error) {
                    console.log(error);
                }
            });
        } else {
            console.log("not document");
        }
    }
});

//editor생성+setdata
function make_editor(res){
    ClassicEditor
        .create(document.querySelector('#Guide_Doc'),
        )
        .then(editor => {
            editor.set('isReadOnly',true);
            doc_editor=editor;
            doc_editor.setData(res);
            return clean_editor();
        })
        .catch(error => {
                console.error(error);
            }
        );
}
function clean_editor(){
    doc_editor.destroy();
}