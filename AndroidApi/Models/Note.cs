using System;
using System.Collections.Generic;

namespace AndroidApi;

public partial class Note
{
    public int Id { get; set; }

    public string Title { get; set; } = null!;

    public string Body { get; set; } = null!;

    public int IDUser { get; set; }
    public int IDCategory { get; set; }
}
