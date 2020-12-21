import re

final_result = ""
with open("index.html","r") as html_page:
    meta_regex = re.compile('<!-- Meta added -->.*<!-- Meta added -->',re.DOTALL)
    html_page_clean = re.sub(meta_regex,"",html_page.read())

    final_result = re.sub("</head>",open("meta.html","r").read().strip()+"</head>",html_page_clean)

    
if(final_result != ""):
    file = open("index.html","w")
    file.write(final_result)
