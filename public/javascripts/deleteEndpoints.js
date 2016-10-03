$(document).on('click', '#delete-talk', function(){
    if (confirm('Are you sure you want to delete this talk?')) {
        var r = jsRoutes.controllers.Application.deleteTalk;
        var id = $(this).attr('value');
        var url = r(id).url;
        deleteItem(id, url);
    }
});

$(document).on('click', '#delete-event', function(){
    if (confirm('Are you sure you want to delete this event?')) {
        var r = jsRoutes.controllers.Application.deleteEvent;
        var id = $(this).attr('value');
        var url = r(id).url;
        deleteItem(id, url);
    }
});

$(document).on('click', '#delete-project', function(){
    if (confirm('Are you sure you want to delete this project?')) {
        var r = jsRoutes.controllers.Application.deleteProject;
        var id = $(this).attr('value');
        var url = r(id).url;
        deleteItem(id, url);
    }
});

function deleteItem(id, url) {
    $.ajax ({
        url: url,
        type: "POST",
        success: function(data) {
            $('tr#'+ id).remove();
        }
    });
}