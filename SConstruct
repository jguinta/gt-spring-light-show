#!/usr/bin/python
import os
import glob
import sys

options = Variables('options.cache', ARGUMENTS)
options.Add(PathVariable('prefix', 'The prefix where the application will be installed', ''))
options.Add(PathVariable('clam_prefix', 'The prefix where CLAM was installed', ''))
options.Add(BoolVariable('release', 'Enabling compiler optimizations', 'no') )
options.Add(BoolVariable('verbose', 'Display the full command line instead a short command description', 'no') )
options.Add(PathVariable('external_dll_path', '(Windows only) The place where the NSIS packager takes the installed DLL from', '.'))
if sys.platform=="linux2" :
	options.Add(BoolVariable('crossmingw', 'Using MinGW crosscompiler mode', 'no') )

def scanFiles(pattern, paths) :
	files = []
	for path in paths :
		files+=glob.glob(os.path.join(path,pattern))
	return files

def recursiveDirs(root) :
	return filter( (lambda a : a.rfind( ".svn")==-1 ),  [ a[0] for a in os.walk(root)]  )

def unique(list) :
	return dict.fromkeys(list).keys()

toolchain='default'
if sys.platform == 'win32': toolchain = 'mingw'
env = Environment(ENV=os.environ, tools=[toolchain], options=options)
options.Save('options.cache', env)
Help(options.GenerateHelpText(env))

env.SConsignFile() # Single signature file

crosscompiling = env.has_key("crossmingw") and env["crossmingw"]
isWindowsPlatform = sys.platform=='win32' or crosscompiling
isLinuxPlatform = sys.platform=='linux2' and not crosscompiling
isDarwinPlatform = sys.platform=='darwin'

CLAMInstallDir = env['clam_prefix']
clam_sconstoolspath = os.path.join(CLAMInstallDir,'share','clam','sconstools')
CLAMVmQtPath = 'vmqt'

env.Tool('qt4', toolpath=[clam_sconstoolspath])
env.Tool('clam', toolpath=[clam_sconstoolspath])
env.Tool('nsis', toolpath=[clam_sconstoolspath])
if sys.platform=='darwin' : env.Tool('bundle', toolpath=[clam_sconstoolspath])
env.Tool('dmg', toolpath=[clam_sconstoolspath])
if crosscompiling :
	env.Tool('crossmingw', toolpath=[clam_sconstoolspath])
sys.path.append(clam_sconstoolspath)
import versionInfo
version, fullVersion = versionInfo.versionFromLocalInfo("chordata")
print "Version: ", version
print "Package version: ", fullVersion
versionInfo.generateVersionSources(os.path.join('src','ChordataVersion'), "Chordata", version, fullVersion)


env['CXXFILESUFFIX'] = '.cxx'
env['QT4_UICDECLSUFFIX'] = '.hxx'
env.moveIntermediateInto('generated')
if not env['verbose']: env.ClamQuietCompilation()
env.activateColorCommandLine()

env.EnableClamModules([
	'clam_core',
	'clam_audioio',
	'clam_processing',
	'clam_qtmonitors',
	], CLAMInstallDir)

env.EnableQt4Modules([
	'QtCore',
	'QtGui',
#	'QtOpenGL',
	'QtSvg',
	],
	debug=False,
	crosscompiling=crosscompiling,
	)

mainSources = {
	'chordata' : os.path.join('src','main.cxx'),
}

sourcePaths = [
	os.path.join('src'),
	os.path.join('src','generated'),
]
extraPaths = []
extraPaths += recursiveDirs(CLAMVmQtPath+"/plot")
extraPaths += recursiveDirs(CLAMVmQtPath+"/render")
extraPaths += recursiveDirs(CLAMVmQtPath+"/data")
extraPaths += recursiveDirs(CLAMVmQtPath+"/util")
extraPaths += recursiveDirs(CLAMVmQtPath+"/player")
extraPaths += recursiveDirs(CLAMVmQtPath+"/misc")
extraPaths += recursiveDirs(CLAMVmQtPath+"/widget")
extraPaths += [
	CLAMInstallDir+'/include',
	CLAMInstallDir+'/include/CLAM', # KLUDGE to keep old style includes
]
includePaths = sourcePaths + extraPaths

sources = scanFiles('*.cxx', sourcePaths)
sources = filter( (lambda a : a.rfind( "moc_")==-1 ),  sources )
sources = filter( (lambda a : a.rfind( "qrc_")==-1 ),  sources )
sources = unique(sources)
for mainSource in mainSources.values() :
	sources.remove(mainSource)

qrcfiles = scanFiles("*.qrc", sourcePaths)
if qrcfiles : sources += env.Qrc(source=qrcfiles)

uifiles = scanFiles("*.ui", sourcePaths)
if uifiles: uiheaders = env.Uic4(source=uifiles)

if isWindowsPlatform :
	sources += env.RES(source=["resources/Chordata.rc"])

env.Append(CPPPATH=includePaths)
env.Append(LIBPATH=[CLAMVmQtPath])
#env.Prepend(LIBS="clam_vmqt4")
if sys.platform=='darwin' :
	env.Append(CPPFLAGS='-DRESOURCES_BASE="\\"Chordata.app/Contents/Resources\\""')
else :
	env.Append(CPPFLAGS='-DRESOURCES_BASE="\\"' + env['prefix'] + '/share/chordata\\""')
if sys.platform!='win32' :
	# TODO: This should not be hardcoded neither prefix (because package intall)
	env.Append(CPPFLAGS='-DDATA_EXAMPLES_PATH="\\"%s\\""'%env['prefix'] + '/share/chordata/example-data')

if sys.platform=='linux2' :
	if env['release'] :
		env.Append( CCFLAGS=['-g','-O3','-fomit-frame-pointer','-Wall'] )
	else :
		env.Append( CCFLAGS=['-g','-O3','-Wall'] )

#commonObjects = env.StaticLibrary(target="chordata", source=sources)
programs = [ env.Program(target=program, source = [main] + sources)
	for program, main in mainSources.items()]

example_data = [
	#'example-data/Chords.pro'
	]

pluginDefines=['-DQT_PLUGIN','-DQT_NO_DEBUG','-DQDESIGNER_EXPORT_WIDGETS','-D_REENTRANT']
env.AppendUnique(CPPFLAGS=pluginDefines)

manpages = [
	'resources/man/man1/chordata.1',
	]

tsfiles = scanFiles("*.ts", ["src/i18n/"])
translatableSources = scanFiles('*.cxx', sourcePaths);
translatableSources+= scanFiles('*.hxx', sourcePaths);
translatableSources+= scanFiles('*.ui', sourcePaths);
translatableSources = filter( (lambda a : a.rfind( "generated/")==-1 ),  translatableSources )
translations = []
if len(tsfiles) :
	# Manual step: lupdate-qt4 *xx *ui -ts Chordata_ca.ts
	#tsNodes = env.Ts(target=tsfiles, source = translatableSources)
	# TODO: move those settings to the qt4 tool
	#env.Precious(tsNodes) # Do not remove it until really regenerated
	#env.NoClean(tsNodes) # They are not just generated but also user edited
	translations = env.Qm(source = tsfiles)

examples = []
for ext in ['pro', 'sc']:
	examples += scanFiles('*.%s'%ext, ['example-data'])

song_examples = []
for ext in ['pool', 'chords', 'mp3', 'wav', 'ogg']:
	song_examples += scanFiles('*.%s'%ext, ['example-data/SongsTest'])

menuEntries = glob.glob('resources/*.desktop')

installation = {
	'/bin' : programs,
	'/share/applications': menuEntries,
	'/share/man/man1' : manpages,
	'/share/annotator/i18n': translations,
	'/share/annotator/example-data': examples,
	'/share/annotator/example-data/SongsTest': song_examples,
}

installTargets = [
	env.Install( env['prefix']+path, files ) for path, files in installation.items() ]

def absolutePosixPathToWine(dir) :
	return 'z:'+'\\\\'.join(dir.split('/'))

if isWindowsPlatform :
	winqtdir=env['QTDIR']
	env.AppendUnique(LINKFLAGS=['-Wl,-subsystem,windows']) # remove the console in windows
	if crosscompiling : env['NSIS_MAKENSIS'] = 'wine ~/.wine/dosdevices/c:/Program\ Files/NSIS/makensis'
	if crosscompiling : winqtdir = absolutePosixPathToWine(winqtdir)
	externalDllPath = env['external_dll_path']
	if crosscompiling : externalDllPath = absolutePosixPathToWine(externalDllPath)
	winclampath = CLAMInstallDir
	if crosscompiling : winclampath = absolutePosixPathToWine(winclampath)
	if crosscompiling :
		env.AddPostAction(programs, env.Action(["i586-mingw32msvc-strip $TARGET"]))
	installTargets += [
		env.Install(
			env['prefix']+"/bin",
			os.path.join(env['QTDIR'],'bin',"Qt"+dll+"4.dll")
			) for dll in 'Core', 'Gui', 'OpenGL', 'Svg']
	env.Append(NSIS_OPTIONS=['/DVERSION=%s' % fullVersion ])
	env.Append(NSIS_OPTIONS=['/DQTDIR=%s'%winqtdir ])
	env.Append(NSIS_OPTIONS=['/DEXTERNALDLLDIR=%s' % externalDllPath ])
	env.Append(NSIS_OPTIONS=['/DCLAMINSTALLDIR=%s' % winclampath ])
	# Get the visual studio runtimes path
	for vcRuntimeDir in os.environ['PATH'].split(";") :
		vcRuntimeDir = os.path.normpath(vcRuntimeDir)
		if os.access(os.path.join(vcRuntimeDir,"msvcr71.dll"),os.R_OK) :
			break
	env.Append(NSIS_OPTIONS=['/DVCRUNTIMEDIR=%s' % vcRuntimeDir ])
	win_packages = [env.Nsis( source='resources/installer.nsi')]
	env.Alias('package', win_packages)

if sys.platform=='darwin' :
	# TODO: Review why those flags were added# TODO: Review why those flags were added# TODO: Review why those flags were added
	env.Append(CPPFLAGS='-DRESOURCES_BASE="\\"Chordata.app/Contents/Resources\\""')
	env.AppendUnique( LINKFLAGS=['-dynamic','-bind_at_load'])

	#TODO install resources
	installTargets = []
	
	mac_bundle = env.Bundle( 
		BUNDLE_NAME='Chordata', 
		BUNDLE_BINARIES=programs,
	#	BUNDLE_RESOURCEDIRS=['foo','bar'],
		BUNDLE_PLIST='resources/Info.plist',
		BUNDLE_ICON='resources/CLAM-Chordata.icns',
	 )
	env.Alias('bundle', mac_bundle)

	#TODO mac_bundle should be dependency of Dmga:	
	arch = os.popen("uname -p").read().strip()
	mac_packages = env.Dmg('CLAM_Chordata-%s-%s.dmg'% (fullVersion, arch), [
		env.Dir('Chordata.app/'),
		env.Dir('example-data/'),
	]+installTargets )
	env.Alias('package', mac_packages)

env.Alias('install', installTargets )

env.Default(programs, translations)

