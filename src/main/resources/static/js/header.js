$(document).ready(function() {
    var url = '';
    if(window.location.pathname.startsWith('/guide')) url = '/guide/tag';
    else url = '/admin/suggestTags';

    $.ajax({
        'url':url,
        'async':false,
        'success': function (data) {
            allTags = $.map(data, function(obj){
                obj.id = obj.id || obj.tag;
                obj.text = obj.text || obj.tag;
                return obj;
            });
        }
    });

    $('.select2-tag').select2({
        'language':'ko',
        'placeholder':'#Tag Search',
        'data':allTags,
        'maximumInputLength': 20,
        'minimumInputLength': 1
    });

    console.log($('.select2-tag').find('option'));
});
//tag select event
$('.select2-tag').on('select2:select', function (e) {
    if(window.location.pathname.startsWith("/admin")){
        location.href="/admin/search?tag=" + encodeURIComponent(e.params.data.tag);
    }
    else {
        location.href="/guide/search?tag=" + encodeURIComponent(e.params.data.tag);
    }
    $('.select2-tag').val(null).trigger("change");
});

$('#admin_tree_button').on('click', function() {
    window.open("/admin/admin_tree","","width=500,height=600,resizable=yes,toolbar=yes,status=0,location=no,menubar=no,scrollbars=yes");
});
