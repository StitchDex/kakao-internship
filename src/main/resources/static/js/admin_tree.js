var hidden_num;
var hidden = new Array();
var token = $("meta[name='_csrf']").attr("content");
$(function () {
    hidden_num=0;
    $('#edit_tree').jstree({
        'core': {
            'multiple': false,
            'check_callback': true,
            'data':  {
                'url' : '/guide/tree',
                'type': "GET",
                'dataType' : 'json',
                'data' : function(node){
                },
                'success': function (data) {
                    for(i=0;i<data.length;i++){
                        var temp=(data[i].state);
                        if(!temp) {
                            hidden[hidden_num++] = data[i].id;
                        }
                    }
                    console.log(data);
                },
            },
        } ,
        'themes': {
            'variant' : 'large',
            'responsive': true
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
        "cookies" : {
            "save_selected" : false,
            'save_opened' : false,
            'auto_save': false
        },
        'contextmenu':{
            "items":  function($node){
                var tree = $("#edit_tree").jstree(true);
                return {
                    "Create": {
                        "separator_before": false,
                        "separator_after": true,
                        "label": "생성",
                        "action": false,
                        "submenu": {
                        "File": {
                            "seperator_before": false,
                                "seperator_after": false,
                                "label": "File",
                                action: function (obj) {
                                $node = tree.create_node($node, {
                                    text: 'NewDOC',
                                    type: 'DOC',
                                    icon: 'far fa-file',
                                    state:{
                                        'disabled': false
                                    }
                                });
                                tree.select_node($node);

                                var data =get_json_data(tree,$node);
                                console.log(data);
                                create_node(data);

                            }
                        },
                        "Folder": {
                            "seperator_before": false,
                                "seperator_after": false,
                                "label": "Folder",
                                action: function (obj) {
                                $node = tree.create_node($node, {
                                    text: 'NewDIR',
                                    type: 'DIR',
                                    icon: 'far fa-folder',
                                    state:{
                                        'disabled': false
                                    }
                                });
                                tree.select_node($node);
                                create_node(get_json_data(tree,$node));
                            }
                        }
                    }
                },
                    "Rename": {
                    "label": "이름수정",
                        "action": function (data) {
                            tree.edit($node,null,function(node,status){
                                update_node(get_json_data(tree,node),node.type);
                            });
                            console.log($node);
                    }
                },
                    "Delete": {
                    "label": "삭제",
                        "action": function (data) {
                            var delnode= $node.id;
                            var title = $node.text;
                            var del = confirm('삭제?');
                            if (del) {
                                if ($node.type != "DIR") {
                                    tree.delete_node($node);
                                    delete_node(delnode,title, "DOC");
                                } else {
                                    if ($node.children.length > 0)
                                        alert('하위 파일이 존재합니다');
                                    else {
                                        tree.delete_node($node);
                                        delete_node(delnode,title, "DIR");
                                    }
                                }

                            }
                        }
                },
                    "Hidden": {
                    "label": "숨김",
                        "action": function (data) {
                        var cur = $node.state.disabled;
                            if($node.children.length >0) {
                                alert('하위 파일이 존재합니다');
                            }
                            else if($node.type != "DIR") {
                                if(!cur)
                                tree.disable_node($node);
                                else{
                                    tree.enable_node($node);
                                }
                                update_node(get_json_data(tree,$node),$node.type);
                            }
                            else{
                                    if(!cur)
                                        tree.disable_node($node);
                                    else{
                                        tree.enable_node($node);
                                    }
                                update_node(get_json_data(tree,$node),$node.type);
                            }
                            console.log($node);
                    }
                }
                }
            }

        },
        'plugins' : ["types","dnd","contextmenu","cookies"]
    }).on('ready.jstree', function(){
        make_disable();
        $(this).jstree('open_all')
    })
        .bind("move_node.jstree", function (e, data) {
            console.log(data);
            var temp = {'id': data.node.id,
                        'parent': data.node.parent,
                        'text': data.node.text,
                        'type': data.node.type,
                        'state': true};
            update_node(temp);
        })

});

//hidden -> disable
function make_disable() {
    for(var i=0;i<hidden.length;i++) {
        var node = $('#edit_tree').jstree(true).get_node(hidden[i]);
        $("#edit_tree").jstree(true).disable_node(node);
        console.log(hidden[i]);
    }
}
function get_json_data(tree,$node){
    var temp = tree.get_node($node);
    var json_data = {
        'id': temp.id,
        'parent': temp.parent,
        'text': temp.text,
        'type': temp.type,
        'state': !temp.state.disabled
    };
    return json_data;
}

function create_node(sendData){
    var title = sendData.text;
    sendData = JSON.stringify(sendData);
    $.ajax({
        url: 'admin/admin_tree/create',
        headers: {"X-CSRF-TOKEN": token},
        data: sendData,
        method: 'POST',
        dataType:'html',
        contentType:'application/json',
        success: function (res) {
            set_Guide_update(title,'create');
            alert("tree create ok");
            opener.document.location.reload();
            location.reload();
        }, error: function (error) {
            console.log(error);
        }
    });
}

function update_node(sendData,what){
    var title = sendData.text;
    sendData = JSON.stringify(sendData);
    $.ajax({
        url: 'admin/admin_tree/update',
        headers: {"X-CSRF-TOKEN": token},
        data: sendData,
        method: 'POST',
        dataType:'html',
        contentType:'application/json',
        success: function (res) {
            set_Guide_update(title,'change');
            alert("tree update ok");
            opener.document.location.reload();
            location.reload();
        }, error: function (error) {
            console.log(error);
        }
    });
}
function delete_node(sendData,title,what){

    sendData = JSON.stringify({'id':sendData, 'type':what});
    $.ajax({
        url: 'admin/admin_tree/delete',
        headers: {"X-CSRF-TOKEN": token},
        data: sendData,
        method: 'POST',
        dataType:'html',
        contentType:'application/json',
        success: function (res) {
            set_Guide_update(title,'delete');
            alert("tree delete ok");
            opener.document.location.reload();
            location.reload();
        }, error: function (error) {
            console.log(error);
        }
    });
}

function create_root(){
    var json_data = {
        'parent': '#',
        'text': 'New Root',
        'type': 'DIR',
        'state': false
    };
    create_node(json_data);
}
