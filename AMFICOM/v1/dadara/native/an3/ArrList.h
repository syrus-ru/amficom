#ifndef _ArrList_H
#define _ArrList_H

/*
 * Ёлементы создает пользователь, а ArrLust только предоставл€ет массив
 * переменной длины со ссылками на элементы.
 * ≈динственный метод дл€ работы с самими элементами - disposeAll() - добавлен
 * исключительно дл€ удобства пользовател€.
 */

class ArrList
{
private:
	void **storage;
	int allocated;
	int used;
public:
	ArrList();
	void disposeAll(); // вспомогательна€ функци€ дл€ удалени€ всех элементов, нулевые ссылки игнорируютс€
	~ArrList(); // удал€ет список, но не элементы
	void *operator[](int id); // доступ к отдельному элементу (getter дл€ ссылки)
	void set(int id, void *obj); // установить новое значение дл€ элемента (setter дл€ ссылки)
	void add(void *obj); // добавл€ет в конец массива новый элемент
	int getLength(); // длина
	void qsort(int(*fcmp)(const void**, const void**)); // вызывает qsort дл€ ссылок
};

/*
 * типичное использование 1:
 *
 * ArrList al = new ArrList;
 * al.add(new Obj(...));
 * al.add(new Obj(...));
 * al.qsort(fcmp);
 * for (i = 0; i < al.getLength(); i++) { ... }
 * al.disposeAll();
 * delete al;
 *
 * типичное использование 2:
 *
 * Obj *objs = new Obj[N];
 * ...
 * ArrList al = new ArrList;
 * for (i = 0; i < N; i++) { al.add(obj[i]); }
 * al.qsort(fcmp);
 * for (i = 0; i < al.getLength(); i++) { ... }
 * delete al;
 * delete[] objs;
 */

#endif


