$(document).on("click", ".btn-add-author", function() {
    var index = $(this).attr('name');
    var newIndex = parseInt(index) + 1;

    var regex = new RegExp(index, 'g');
    var authorGroup = $('#author-group-' + index).wrap('<div>').parent().html().replace(regex, newIndex);
    $('#author-group-' + index).unwrap();
    $('#authors-container').find('.col-md-10').append(authorGroup);
    $(this).remove();
});
