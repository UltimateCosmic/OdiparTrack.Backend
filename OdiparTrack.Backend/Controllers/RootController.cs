using Microsoft.AspNetCore.Mvc;

[ApiController]
[Route("/")]
public class RootController : ControllerBase
{
    [HttpGet]
    public IActionResult Get()
    {
        return Ok("Backend is running");
    }
}
