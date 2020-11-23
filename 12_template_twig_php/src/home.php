 

<p>

    Subscribe to our notification mailing-list.
    <form method='post' action=''>
        <div class="form-group"> 
            <input placeholder="Enter Your Email (you@re.aweso.me)" name="email" size=50></input> <button class="btn btn-default" type="submit" name='submit'>Submit Button</button>
       </div> 
    </form>

<?php
include('vendor/twig/twig/lib/Twig/Autoloader.php');
if (isset($_POST['email'])) {
    $email=$_POST['email'];

    Twig_Autoloader::register();
    try {
        $loader = new Twig_Loader_String();
        $twig = new Twig_Environment($loader);

        $result= $twig->render("Thanks {$email}. You will be notified soon.");
        echo $result;

    } catch (Exception $e) {
        echo $e->getMessage();
    }
}

?>
</p>