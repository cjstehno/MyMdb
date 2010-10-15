//
// builds bulk loader templates for mymdb
//

def box = args[0]
def dir = new File('c:/Users/cjstehno/Desktop')

def filidx = 1
def outfile = new File(dir,"movies-${filidx}.txt")

def idx = args[1] as Integer
new File(dir,'titles.txt').eachLine { line ->
    def titles = parseLine(line)
    titles.each { title ->
        writeRecord(outfile,title,box,idx)
    }
    
    if( idx % 30 == 0){
        filidx++
        outfile = new File(dir,"movies-${filidx}.txt")
    }
    
    idx++
}

def parseLine( line ){
    def titles = []
    if( line.indexOf('/') != -1 ){
        line.split(' / ').each {
            titles << it.trim()
        }
    } else {
        titles << line.trim()
    }
}

def writeRecord( f, title, box, idx ){
    def str = ''
    str += "t:${title}\n"
    str += 'y:\n'
    str += "s:${box},${idx}\n"
    str += 'g:\n'
    str += 'g:\n'
    str += 'd:\n'
    str += 'a:\n'
    str += 'a:\n'
    str += 'a:\n'
    str += 'a:\n'
    str += 'p:\n'
    str += '==\n' 
    f.append str
}