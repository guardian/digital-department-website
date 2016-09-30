$(document).on("click", ".btn-add-author", function() {
    var index = $(".author").length;
    var regex = new RegExp(0, "g");
    var newFields = $(".author__fields").clone().html().replace(regex, index)

    $(".author").last().after( $(".author").first().clone())

    //last is now the new author
    $(".author").last().find(".author__fields").replaceWith(newFields)
    $(".author").last().find("input").val("").end()

});
