$(document).ready(function() {
    $('.select2-tag').select2({
        'placeholder':"Search Tag",
        'data' : [
            {
                "text" : "Group1",
                "children" : [{"id" : "1","text" : "opt1"}, {"id" : "2", "text":"opt2"}],
                "element" : HTMLOptGroupElement
            }
        ]
    });
});