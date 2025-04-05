#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <limits.h>
#pragma warning(disable:4996)

// ================== 排序算法实现 ==================
void insertSort(int* arr, int n) {
    for (int i = 1; i < n; i++) {
        int key = arr[i];
        int j = i - 1;
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = key;
    }
}

void merge(int* arr, int l, int m, int r) {
    int n1 = m - l + 1;
    int n2 = r - m;
    int* L = (int*)malloc(n1 * sizeof(int));
    int* R = (int*)malloc(n2 * sizeof(int));

    for (int i = 0; i < n1; i++) L[i] = arr[l + i];
    for (int j = 0; j < n2; j++) R[j] = arr[m + 1 + j];

    int i = 0, j = 0, k = l;
    while (i < n1 && j < n2)
        arr[k++] = (L[i] <= R[j]) ? L[i++] : R[j++];
    while (i < n1) arr[k++] = L[i++];
    while (j < n2) arr[k++] = R[j++];

    free(L);
    free(R);
}

void mergeSortHelper(int* arr, int l, int r) {
    if (l < r) {
        int m = l + (r - l) / 2;
        mergeSortHelper(arr, l, m);
        mergeSortHelper(arr, m + 1, r);
        merge(arr, l, m, r);
    }
}

void mergeSort(int* arr, int n) {
    mergeSortHelper(arr, 0, n - 1);
}

int partition(int* arr, int low, int high) {
    int pivot = arr[high];
    int i = low - 1;
    for (int j = low; j < high; j++) {
        if (arr[j] < pivot) {
            i++;
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
    int temp = arr[i + 1];
    arr[i + 1] = arr[high];
    arr[high] = temp;
    return i + 1;
}

void quickSortHelper(int* arr, int low, int high) {
    if (low < high) {
        int pi = partition(arr, low, high);
        quickSortHelper(arr, low, pi - 1);
        quickSortHelper(arr, pi + 1, high);
    }
}

void quickSort(int* arr, int n) {
    quickSortHelper(arr, 0, n - 1);
}

void countSort(int* arr, int n) {
    int max = INT_MIN, min = INT_MAX;
    for (int i = 0; i < n; i++) {
        if (arr[i] > max) max = arr[i];
        if (arr[i] < min) min = arr[i];
    }

    int range = max - min + 1;
    int* count = (int*)calloc(range, sizeof(int));

    for (int i = 0; i < n; i++)
        count[arr[i] - min]++;

    int index = 0;
    for (int i = 0; i < range; i++) {
        while (count[i]--) {
            arr[index++] = i + min;
        }
    }
    free(count);
}

void radixCountSort(int* arr, int n) {
    int max = INT_MIN;
    for (int i = 0; i < n; i++)
        if (arr[i] > max) max = arr[i];

    for (int exp = 1; max / exp > 0; exp *= 10) {
        int* output = (int *)malloc(n * sizeof(int));
        int count[10] = { 0 };

        for (int i = 0; i < n; i++)
            count[(arr[i] / exp) % 10]++;

        for (int i = 1; i < 10; i++)
            count[i] += count[i - 1];

        for (int i = n - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }

        memcpy(arr, output, n * sizeof(int));
        free(output);
    }
}

// ================== 测试工具函数 ==================
void generateArray(int* arr, int n, int max_val) {
    for (int i = 0; i < n; i++)
        arr[i] = rand() % max_val;
}

void testSort(void (*sortFunc)(int*, int), int* arr, int n, const char* name) {
    int* temp = (int*)malloc(n * sizeof(int));
    memcpy(temp, arr, n * sizeof(int));

    clock_t start = clock();
    sortFunc(temp, n);
    double time = (double)(clock() - start) / CLOCKS_PER_SEC;

    printf("%-16s 数据量: %-6d 耗时: %.4fs\n", name, n, time);
    free(temp);
}

// ================== 文件操作 ==================
void generateDataFile(const char* filename, int n) {
    FILE* fp = fopen(filename, "w");
    for (int i = 0; i < n; i++)
        fprintf(fp, "%d\n", rand() % 10000);
    fclose(fp);
}

int* readDataFile(const char* filename, int* n) {
    FILE* fp = fopen(filename, "r");
    int capacity = 10000;
    int* arr = (int *)malloc(capacity * sizeof(int));
    int num, count = 0;

    while (fscanf(fp, "%d", &num) != EOF) {
        if (count == capacity) {
            capacity *= 2;
            arr = (int *)realloc(arr, capacity * sizeof(int));
        }
        arr[count++] = num;
    }

    *n = count;
    fclose(fp);
    return arr;
}

// ================== 排序应用题 ==================
int countInversions(int* arr, int n) {
    int count = 0;
    for (int i = 0; i < n; i++)
        for (int j = i + 1; j < n; j++)
            if (arr[i] > arr[j]) count++;
    return count;
}

int quickSelect(int* arr, int l, int r, int k);

int findKthSmallest(int* arr, int n, int k) {
    int* copy = (int *)malloc(n * sizeof(int));
    memcpy(copy, arr, n * sizeof(int));
    int result = quickSelect(copy, 0, n - 1, k - 1);
    free(copy);
    return result;
}

int quickSelect(int* arr, int l, int r, int k) {
    if (l == r) return arr[l];
    int pivot = arr[r];
    int i = l;
    for (int j = l; j < r; j++) {
        if (arr[j] <= pivot) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
            i++;
        }
    }
    int temp = arr[i];
    arr[i] = arr[r];
    arr[r] = temp;

    if (k == i) return arr[i];
    else if (k < i) return quickSelect(arr, l, i - 1, k);
    else return quickSelect(arr, i + 1, r, k);
}

// ================== 主测试程序 ==================
int main() {
    srand(time(NULL));
    const int sizes[] = { 10000, 50000, 200000 };
    const int test_count = sizeof(sizes) / sizeof(sizes[0]);

    // 大数据量测试
    for (int i = 0; i < test_count; i++) {
        int n = sizes[i];
        int* arr = (int *)malloc(n * sizeof(int));
        generateArray(arr, n, 10000);

        printf("\n=== 大数据量测试: %d 数据 ===\n", n);
        testSort(insertSort, arr, n, "插入排序");
        testSort(mergeSort, arr, n, "归并排序");
        testSort(quickSort, arr, n, "快速排序");
        testSort(countSort, arr, n, "计数排序");
        testSort(radixCountSort, arr, n, "基数排序");

        free(arr);
    }

    // 小数据量多次测试
    printf("\n=== 小数据量多次测试 ===\n");
    const int small_size = 100;
    const int iterations = 100000;
    int* small_arr = (int *)malloc(small_size * sizeof(int));
    generateArray(small_arr, small_size, 1000);

    clock_t start = clock();
    for (int i = 0; i < iterations; i++) {
        int* temp = (int *)malloc(small_size * sizeof(int));
        memcpy(temp, small_arr, small_size * sizeof(int));
        insertSort(temp, small_size);
        free(temp);
    }
    printf("插入排序 100数据 × 10万次: %.4fs\n",
        (double)(clock() - start) / CLOCKS_PER_SEC);

    printf("\n=== 文件操作测试 ===\n");
    generateDataFile("testdata.txt", 10000);
    int file_size;
    int* file_data = readDataFile("testdata.txt", &file_size);
    printf("从文件读取 %d 条数据，首元素: %d，尾元素: %d\n",
        file_size, file_data[0], file_data[file_size - 1]);
    free(file_data);

    printf("\n=== 排序应用 ===\n");
    int test_arr[] = { 3, 1, 4, 1, 5, 9, 2, 6 };
    int test_size = sizeof(test_arr) / sizeof(test_arr[0]);
    printf("逆序对数量: %d\n", countInversions(test_arr, test_size));
    printf("第3小元素: %d\n", findKthSmallest(test_arr, test_size, 3));

    return 0;
}