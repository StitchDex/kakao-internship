$(document).ready(function() {
    //load #Guide_Doc
    var doc_key = $('#selected').val();
    $.ajax({
        'url':'/guide/menu',
        'data':{'doc_key':doc_key},
        'success':function (res) {
            var title = res.title;
            res = res.text;
            $('#guide-title').text(title);
            if(admin_editor!=null){ // change doc while edit
                admin_editor.destroy(true);
                make_editor(res);
            }
            else if(doc_editor!=null){ // change doc
                doc_editor.destroy();
                make_editor(res);
            }
            else{ // document_ready
                make_editor(res);
            }
            get_Guide_update(selectedText);
            init_select_tagging();
        },
        'error':function () {

        },
    });
})
