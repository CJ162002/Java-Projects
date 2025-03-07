// function for sidebar 
const togglesidebar=()=> {
if($(".sidebar").is(":visible"))
{
	$(".sidebar").css("display","none")
	$(".content_div").css("width", "100%")
} else {
    $(".sidebar").css("display","block")
	$(".content_div").css("width", "82%")
}
};



// Function to fade out alerts after a delay
    function fadeOutAlerts() {
        const errorAlert = document.getElementById('error-alert');
        const logoutAlert = document.getElementById('logout-alert');

        if (errorAlert) {
            setTimeout(() => {
                errorAlert.style.opacity = '0';
                setTimeout(() => errorAlert.style.display = 'none', 500); // Adjust delay if needed
            }, 3000); // Delay before fading out (3 seconds)
        }

        if (logoutAlert) {
            setTimeout(() => {
                logoutAlert.style.opacity = '0';
                setTimeout(() => logoutAlert.style.display = 'none', 500); // Adjust delay if needed
            }, 3000); // Delay before fading out (3 seconds)
        }
    };

    // Call the function to fade out alerts
    window.onload = fadeOutAlerts;

// Function to fade out alerts

	function contactsFadeMessage() {
		const showMessage = document.getElementById('showcontact_message');
		
		if(showMessage){
			setTimeout(() => {
				showMessage.style.opacity = '0';
				setTimeout(() => showMessage.style.display = 'none', 500);
			},3000);
		}
	}
	window.onload = contactsFadeMessage;
	
	
// Confirmation box 
function confirmBox(cid) {
	swal({
	  title: "Are you sure?",
	  text: "Once deleted, you will not be able to recover this contact",
	  icon: "warning",
	  buttons: true,
	  dangerMode: true,
	})
	.then((willDelete) => {
	  if (willDelete) {
		window.location = "/user/delete/"+cid;
	    } else {
			
	  }
	});
}
