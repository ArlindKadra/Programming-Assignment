<?php
// include the config.ini file and if any errors show up write on the log
try
{
includeFile("config.ini");
}
catch(Exception $e)
{
	fwrite(fopen("log.txt","a"),date("D/M/Y H:i:s") . " ERROR: Config file not found" .PHP_EOL);
	die();
}
// if the debug variable is 1, write information about the request on the log
if(DEBUG)
{
	fwrite(fopen("log.txt","a"),date("D/M/Y H:i:s") . " ". (isset($_POST)?"POST":"GET") . " Request from " . $_SERVER['REMOTE_ADDR'] . " " . print_r($_REQUEST, true)  . PHP_EOL);
}
// Check if Post variable getCommands is true and is not empty and return the random command in json encoding
if(!empty($_POST["getCommands"]) && ($_POST["getCommands"]==='true'))
{
	
	$commands = array( 
			
						array("cmd" => "soundOne"),
						array("cmd" => "soundTwo"),
						array("cmd" => "text", "text" => "Igitur qui desiderat pacem, praeparet bellum"),
						array("cmd" => "color", "color" => randColor())
			
						);
						
						
	$out = json_encode($commands[mt_rand(0, count($commands) - 1)]);
		
	if (DEBUG)
	{
				fwrite(fopen("log.txt", "a"), "RESPONSE: " . $out . PHP_EOL);
	}
			
	echo "$out";	
			
			
			
			
			
		
}
else
{
        http_response_code(404); /* file not found */
}
// function that returns a random hexadecimal number
function randColor() 
{
	return sprintf("#%06X", mt_rand(0, 0xFFFFFF));
}
function includeFile($file)
{
	
    if(file_exists($file))
        {
				include_once($file);
        }
    else
        {
                throw(new Exception('File does not exist.'));
        }
}  
	

?>
