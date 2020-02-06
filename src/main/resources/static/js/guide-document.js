$(document).ready(function () {
    hidden_num=0;
    depth2Dir=0;
    var doc_key = $('#selected').val();
    $('#jstree').jstree({
        'core': {
            'multiple': false,
            "check_callback": true,
            'themes': {
                'icon' : true,
                'responsive': true
            },
            'data':  {
                'url' : '/guide/tree',
                'type': "GET",
                'dataType' : 'json',
                'data' : function(node){
                    return {"id": node.id == "#" ? "IAM" : node.id };
                },
                'success': function (data) {
                    for(i=0;i<data.length;i++){
                        var temp =(data[i].state);
                        if(!temp) {
                            hidden[hidden_num++] = data[i].id;
                        }
                    }
                    console.log(data);
                }
            }
        },
        'types' : {
            "DIR": {
                "icon" : "far fa-folder"
            },
            "DOC": {
                "icon" : "far fa-file",
                "max_children" : 0,
            },
        },
        'plugins' : ["types", "state","wholerow"]
    })
        .on('ready.jstree', function(){
            let cur = $('#jstree').jstree('get_node','DOC'+doc_key);
            before_tree_open();
            $('#jstree').jstree('open_node', 'DIR0', function(e, data) {
            }, true);
            make_selected(doc_key.cur);
        });

    //load #Guide_Doc
    var doc_key = $('#selected').val();
    $.ajax({
        'url': '/guide/menu',
        'data': {'doc_key': doc_key},
        'success': function (res) {
            var title = res.title;
            res = res.text;
            $('#guide-title').text(title);
            if (admin_editor != null) { // change doc while edit
                admin_editor.destroy(true);
                make_editor(res);
            } else if (doc_editor != null) { // change doc
                doc_editor.destroy();
                make_editor(res);
            } else { // document_ready
                make_editor(res);
            }
            get_Guide_update(selectedText);

        },
        'error': function () {

        },
    });

});
function make_selected(doc_key,cur) {

    $('#jstree').jstree('open_node')
}