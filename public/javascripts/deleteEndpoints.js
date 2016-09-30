$(document).on('click', '#delete-talk', function(){
    if (confirm('Are you sure you want to delete this talk?')) {
        var r = jsRoutes.controllers.Application.deleteTalk;
        var id = $(this).attr('value');
        var url = r(id).url;
        $.ajax ({
            url: url,
            type: "POST",
            success: function(data) {
                $('tr#'+ id).remove();
            }
        });
    }
});