
jQuery(function(){
	$('#actor-tabs').tabs();

    $('#poster-dialog').dialog({
        autoOpen:false,
        modal:true,
        minWidth:100
    });

    $('img.movie-poster').click(function(evt){
        $('#poster-dialog').html("<img src='" + evt.target.src + "' />");
        $('#poster-dialog').dialog('open');
    });
});