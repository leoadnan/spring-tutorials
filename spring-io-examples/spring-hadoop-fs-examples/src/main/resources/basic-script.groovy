name = UUID.randomUUID().toString()
doesntWork = new File(".").getCanonicalPath()
println doesntWork
scriptName = "src/main/resources/application.properties"
//scriptName = this.getClass().getResource( 'application.properties' ).text 
//println scriptName
fs.copyFromLocalFile(scriptName,name)
dir = "script-dir"
if(!fsh.test(dir)){
	fs.mkdirs(dir)
	fsh.cp(name,dir)
	fsh.chmodr(700,dir)
	println "File content is: "+fsh.cat(dir+"/"+name).toString()
}

distcp.copy('my.file','data/')
println fsh.ls(dir).toString()
fsh.rmr(dir)